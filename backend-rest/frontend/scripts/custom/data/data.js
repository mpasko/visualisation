define(["dojo/dom", "dojo/store/Memory", "dijit/registry", "custom/grid", "dgrid/editor", "dijit/form/TextBox", "custom/data/cellRenderers", "custom/data/attributes", "dojo/dom-construct"], function(dom, Memory, registry, grid, editor, TextBox, cellRenderers, utils, domConstruct) {
  var internal, module;
  internal = {
    stores: {
      attr: new Memory(),
      domains: new Memory(),
      events: new Memory()
    },
    updateDataGrid: function() {
      var attribute, attributes, column, columns, eventsGrid, selected_attributes;
      attributes = this.stores.attr.query({});
      selected_attributes = this.stores.attr.query({
        selected: true
      });
      columns = (function() {
        var _i, _len, _results;
        _results = [];
        for (_i = 0, _len = selected_attributes.length; _i < _len; _i++) {
          attribute = selected_attributes[_i];
          _results.push({
            field: 'attribute' + (attributes.indexOf(attribute) + 1),
            label: attribute.name,
            autoSave: true,
            renderCell: cellRenderers.get(attribute, internal.stores.domains.query({
              name: attribute.domain
            }))
          });
        }
        return _results;
      })();
      registry.byId("events").destroyDescendants(false);
      domConstruct.create("div", {
        id: "datagrid",
        style: "height : 100%;"
      }, "events");
      eventsGrid = new grid.paginated({
        store: this.stores.events,
        columns: (function() {
          var _i, _len, _results;
          _results = [];
          for (_i = 0, _len = columns.length; _i < _len; _i++) {
            column = columns[_i];
            _results.push(editor(column, TextBox, "click"));
          }
          return _results;
        })(),
        pagingLinks: 3,
        firstLastArrows: true,
        rowsPerPage: 20,
        pageSizeOptions: [20, 50, 100]
      }, "datagrid");
      return eventsGrid.startup();
    }
  };
  module = {
    updateStores: function(input) {
      var attribute, _i, _len, _ref;
      _ref = input.attributes;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        attribute = _ref[_i];
        attribute["selected"] = true;
      }
      console.log(input.attributes);
      internal.stores.attr.setData(input.attributes);
      internal.stores.domains.setData(input.domains);
      internal.stores.events.setData(input.events);
      internal.updateDataGrid();
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
        store: internal.stores.stats,
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
        array = [];
        array.push({
          id: "Domain",
          value: desc.domain
        });
        array.push({
          id: "Base domain",
          value: desc.baseDomain
        });
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
        var el;
        el = event.cell.row.data;
        el[event.cell.column.field] = event.value;
        internal.stores.attr.put(el);
        return internal.updateDataGrid();
      });
    }
  };
  return module;
});
