define(["dijit/registry", "dojo/dom-construct", "custom/grid", "dgrid/editor", "custom/data/attributes", "dijit/form/TextBox"], function(registry, domConstruct, grid, editor, attributeUtils, TextBox) {
  var internal, module;
  internal = {
    get: function(attribute, domain_query) {
      var a, baseDomain, baseParameters;
      baseDomain = attributeUtils.getBaseDomain(attribute, domain_query);
      baseParameters = attributeUtils.getBaseParameters(attribute, domain_query);
      a = this.partialRight(this[baseDomain], attribute.name);
      return internal.partialRight(a, baseParameters);
    },
    partialRight: function(fn, args) {
      var aps;
      aps = Array.prototype.slice;
      args = aps.call(arguments, 1);
      return function() {
        return fn.apply(this, aps.call(arguments).concat(args));
      };
    },
    nominal: function(object, value, node, options, parameters, attr_name) {
      var div;
      div = document.createElement("div");
      div.className = "renderedCell";
      div.innerHTML = value;
      return div;
    },
    linear: function(object, value, node, options, parameters, attr_name) {
      var div;
      div = document.createElement("div");
      div.className = "renderedCell";
      div.innerHTML = value;
      return div;
    },
    continuous: function(object, value, node, options, parameters, attr_name) {
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
  module = {
    setup: function() {},
    update: function(stores) {
      var attribute, attributes, column, columns, eventsGrid, pad, selected_attributes;
      attributes = stores.attr.query({});
      selected_attributes = stores.attr.query({
        selected: true
      });
      pad = function(n) {
        if (n < 10) {
          return '0' + n;
        } else {
          return '' + n;
        }
      };
      columns = (function() {
        var _i, _len, _results;
        _results = [];
        for (_i = 0, _len = selected_attributes.length; _i < _len; _i++) {
          attribute = selected_attributes[_i];
          _results.push({
            field: 'attribute' + pad(attributes.indexOf(attribute) + 1),
            label: attribute.name,
            autoSave: true,
            renderCell: internal.get(attribute, stores.domains.query({
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
        store: stores.events,
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
