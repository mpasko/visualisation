define(["dijit/registry", "dojo/dom-construct", "custom/grid", "dgrid/editor", "custom/data/attributes", "dijit/form/TextBox"], function(registry, domConstruct, grid, editor, utils, TextBox) {
  var internal, module;
  internal = {
    renderers: {},
    get: function(attribute, domain_query) {
      var baseDomain, baseParameters;
      baseDomain = utils.getBaseDomain(attribute, domain_query);
      baseParameters = utils.getBaseParameters(attribute, domain_query);
      return internal.partialRight(this.renderers[baseDomain], baseParameters);
    },
    partialRight: function(fn, args) {
      var aps;
      aps = Array.prototype.slice;
      args = aps.call(arguments, 1);
      return function() {
        return fn.apply(this, aps.call(arguments).concat(args));
      };
    },
    plainCellRenderer: function(object, value, node, options, parameters) {
      var div;
      div = document.createElement("div");
      div.className = "renderedCell";
      div.innerHTML = value;
      return div;
    },
    numberCellRenderer: function(object, value, node, options, parameters) {
      var color, div, max, min, val;
      div = document.createElement("div");
      div.className = "renderedCell";
      min = parseFloat(parameters[0]);
      max = parseFloat(parameters[1]);
      val = Math.round(100 * ((parseFloat(value)) - min) / (max - min));
      if (val < 33) {
        color = "#3FFF00";
      } else if (val < 66) {
        color = "#FFD300";
      } else {
        color = "#ED1C24";
      }
      div.style.backgroundColor = color;
      div.style.width = val + "%";
      div.style.textAlign = "center";
      div.style.borderRadius = "15px";
      div.innerHTML = value;
      return div;
    }
  };
  internal.renderers = {
    nominal: internal.plainCellRenderer,
    linear: internal.plainCellRenderer,
    integer: internal.numberCellRenderer,
    continuous: internal.numberCellRenderer
  };
  module = {
    setup: function(stores) {
      return internal.stores = stores;
    },
    update: function() {
      var column, columns, eventsGrid, item, mapping;
      mapping = utils.getMapping(internal.stores, ["linear", "nominal", "integer", "continuous"]);
      columns = (function() {
        var _i, _len, _results;
        _results = [];
        for (_i = 0, _len = mapping.length; _i < _len; _i++) {
          item = mapping[_i];
          _results.push({
            field: item.field,
            label: item.attribute.name,
            autoSave: true,
            renderCell: internal.get(item.attribute, internal.stores.domains.query({
              name: item.attribute.domain
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
        store: internal.stores.simplifiedEvents,
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
  return module;
});
