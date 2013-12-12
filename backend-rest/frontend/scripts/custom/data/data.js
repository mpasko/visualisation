define(["dojo/store/Memory", "dijit/registry", "custom/grid", "dgrid/editor", "dijit/form/TextBox", "custom/data/attributes", "custom/data/dataGrid"], function(Memory, registry, grid, editor, TextBox, utils, datagrid) {
  var internal, module;
  internal = {
    stores: {
      attr: new Memory(),
      domains: new Memory(),
      events: new Memory()
    },
    visualisations: [datagrid]
  };
  module = {
    update: function(input) {
      var attribute, item, _i, _j, _len, _len1, _ref, _ref1;
      _ref = input.attributes;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        attribute = _ref[_i];
        attribute["selected"] = true;
      }
      console.log(input.attributes);
      internal.stores.attr.setData(input.attributes);
      internal.stores.domains.setData(input.domains);
      internal.stores.events.setData(input.events);
      _ref1 = internal.visualisations;
      for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
        item = _ref1[_j];
        item.update(internal.stores);
      }
      registry.byId("statistics").refresh();
      registry.byId("attributes").refresh();
      return registry.byId("domains").refresh();
    },
    collectForExperiment: function(collect) {
      var a, attributes, _i, _len;
      attributes = JSON.parse(JSON.stringify(internal.stores.attr.query({})));
      for (_i = 0, _len = attributes.length; _i < _len; _i++) {
        a = attributes[_i];
        delete a.selected;
      }
      return collect({
        attributes: attributes,
        domains: internal.stores.domains.query({}),
        events: internal.stores.events.query({})
      });
    },
    setup: function() {
      var attr_grid, domains_grid, statistics_grid;
      attr_grid = new grid.onDemand({
        store: internal.stores.attr,
        columns: [
          editor({
            field: "name",
            label: "Attribute value",
            autoSave: true
          }, TextBox, "dblclick"), editor({
            field: "selected",
            label: "Selected",
            autoSave: true
          }, 'checkbox')
        ]
      }, "attributes");
      domains_grid = new grid.onDemand({
        store: internal.stores.domains,
        columns: [
          {
            field: "name",
            label: "Name"
          }, {
            field: "domain",
            label: "Domain"
          }, {
            field: "parameters",
            label: "Parameters"
          }
        ]
      }, "domains");
      statistics_grid = new grid.simple({
        columns: [
          {
            field: "id",
            label: "Statistic"
          }, {
            field: "value",
            label: "Value"
          }
        ]
      }, "statistics");
      attr_grid.on("dgrid-select", function(event) {
        var a, array, attribute, desc, domain_query, _i, _j, _k, _len, _len1, _len2, _ref, _ref1, _ref2;
        attribute = event.rows[0].data;
        domain_query = internal.stores.domains.query({
          name: attribute.domain
        });
        desc = utils.extractAttributeDescription(attribute, domain_query);
        array = [
          {
            id: "Domain",
            value: desc.domain
          }, {
            id: "Base domain",
            value: desc.baseDomain
          }
        ];
        switch (desc.baseDomain) {
          case "linear":
            _ref = utils.getDiscreteValues(desc.parameters);
            for (_i = 0, _len = _ref.length; _i < _len; _i++) {
              a = _ref[_i];
              array.push(a);
            }
            break;
          case "nominal":
            _ref1 = utils.getDiscreteValues(desc.parameters);
            for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
              a = _ref1[_j];
              array.push(a);
            }
            break;
          case "continuous":
            _ref2 = utils.getContinuousValues(desc.parameters);
            for (_k = 0, _len2 = _ref2.length; _k < _len2; _k++) {
              a = _ref2[_k];
              array.push(a);
            }
        }
        registry.byId("statistics").refresh();
        return registry.byId("statistics").renderArray(array);
      });
      return attr_grid.on("dgrid-datachange", function(event) {
        var el, item, _i, _len, _ref, _results;
        el = event.cell.row.data;
        el[event.cell.column.field] = event.value;
        internal.stores.attr.put(el);
        _ref = internal.visualisations;
        _results = [];
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          item = _ref[_i];
          _results.push(item.update(internal.stores));
        }
        return _results;
      });
    }
  };
  return module;
});
