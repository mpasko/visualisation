define(["dojo/dom", "dojo/store/Memory", "dijit/registry", "custom/grid", "dgrid/editor", "dijit/form/TextBox", "custom/data/cellRenderers", "custom/data/attributes", "dojo/dom-construct"], function(dom, Memory, registry, grid, editor, TextBox, cellRenderers, utils, domConstruct) {
  var internal, module;
  internal = {
    stats_store: new Memory(),
    attr_store: new Memory(),
    domains_store: new Memory(),
    events_store: new Memory(),
    updateDataGrid: function(recreate) {
      var attribute, attributes, column, columns, eventsGrid, selected_attributes;
      attributes = this.attr_store.query({});
      selected_attributes = this.attr_store.query({
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
            renderCell: cellRenderers.get(attribute, internal.domains_store.query({
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
        store: this.events_store,
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
      internal.attr_store.setData(input.attributes);
      internal.domains_store.setData(input.domains);
      internal.events_store.setData(input.events);
      internal.stats_store.setData([]);
      internal.updateDataGrid(true);
      registry.byId("statistics").refresh();
      registry.byId("attributes").refresh();
      return registry.byId("domains").refresh();
    },
    collectForExperiment: function(collect) {
      var a, attributes, _i, _len;
      attributes = JSON.parse(JSON.stringify(internal.attr_store.query({})));
      for (_i = 0, _len = attributes.length; _i < _len; _i++) {
        a = attributes[_i];
        delete a.selected;
      }
      return collect({
        attributes: attributes,
        domains: internal.domains_store.query({}),
        events: internal.events_store.query({})
      });
    },
    setup: function() {
      var attr_grid, domains_grid, statistics_grid;
      attr_grid = new grid.onDemand({
        store: internal.attr_store,
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
        store: internal.domains_store,
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
      statistics_grid = new grid.onDemand({
        store: internal.stats_store,
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
        var a, attribute, desc, domain_query, _i, _j, _k, _len, _len1, _len2, _ref, _ref1, _ref2;
        attribute = event.rows[0].data;
        domain_query = internal.domains_store.query({
          name: attribute.domain
        });
        desc = utils.extractAttributeDescription(attribute, domain_query);
        internal.stats_store.setData([]);
        internal.stats_store.put({
          id: "Domain",
          value: desc.domain
        });
        internal.stats_store.put({
          id: "Base domain",
          value: desc.baseDomain
        });
        switch (desc.baseDomain) {
          case "linear":
            _ref = utils.getDiscreteValues(desc.parameters);
            for (_i = 0, _len = _ref.length; _i < _len; _i++) {
              a = _ref[_i];
              internal.stats_store.put(a);
            }
            break;
          case "nominal":
            _ref1 = utils.getDiscreteValues(desc.parameters);
            for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
              a = _ref1[_j];
              internal.stats_store.put(a);
            }
            break;
          case "continuous":
            _ref2 = utils.getContinuousValues(desc.parameters);
            for (_k = 0, _len2 = _ref2.length; _k < _len2; _k++) {
              a = _ref2[_k];
              internal.stats_store.put(a);
            }
        }
        return registry.byId("statistics").refresh();
      });
      return attr_grid.on("dgrid-datachange", function(event) {
        var el;
        el = event.cell.row.data;
        el[event.cell.column.field] = event.value;
        internal.attr_store.put(el);
        return internal.updateDataGrid(false);
      });
    }
  };
  return module;
});
