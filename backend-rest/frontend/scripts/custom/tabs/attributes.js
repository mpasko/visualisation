define(["dojo/dom", "dojo/_base/window", "dojo/topic", "dojo/store/Memory", "dijit/registry", "custom/grid"], function(dom, win, topic, Memory, registry, grid) {
  var internal, module;
  internal = {
    stats_store: new Memory(),
    attr_store: new Memory(),
    domains_store: new Memory(),
    hasDefaultDomain: function(attribute) {
      var _ref;
      return (_ref = attribute.domain) === "linear" || _ref === "nominal" || _ref === "continuous";
    },
    isDiscrete: function(attribute) {
      return attribute === "linear" || attribute === "nominal";
    },
    getBaseDomain: function(attribute) {
      if (this.hasDefaultDomain(attribute)) {
        return attribute.domain;
      } else {
        return this.domains_store.query({
          name: attribute.domain
        })[0].domain;
      }
    },
    getBaseParameters: function(attribute) {
      var parameter, parameters, _i, _len, _ref, _results;
      if (this.hasDefaultDomain(attribute)) {
        parameters = attribute.parameters;
      } else {
        parameters = this.domains_store.query({
          name: attribute.domain
        })[0].parameters;
      }
      _ref = /([^{}]+)/.exec(parameters)[1].split(",");
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        parameter = _ref[_i];
        _results.push(parameter.trim());
      }
      return _results;
    },
    nominal: function(parameters) {
      return console.log("nominal");
    },
    linear: function(parameters) {
      return console.log("linear");
    },
    continuous: function(parameters) {
      this.stats_store.put({
        id: "Minimum",
        value: parameters[0]
      });
      this.stats_store.put({
        id: "Maximum",
        value: parameters[1]
      });
      return registry.byId("statistics").refresh();
    },
    basicStats: function(attribute) {
      return this[this.getBaseDomain(attribute)](this.getBaseParameters(attribute));
    },
    partialRight: function(fn, args) {
      var aps;
      aps = Array.prototype.slice;
      args = aps.call(arguments, 1);
      return function() {
        return fn.apply(this, aps.call(arguments).concat(args));
      };
    },
    cellRenderers: {
      nominal: function(object, value, node, options, parameters, attr_name) {
        var div;
        div = document.createElement("div");
        div.className = "renderedCell";
        div.innerHTML = object[attr_name];
        return div;
      },
      linear: function(object, value, node, options, parameters, attr_name) {
        var div;
        div = document.createElement("div");
        div.className = "renderedCell";
        div.innerHTML = object[attr_name];
        return div;
      },
      continuous: function(object, value, node, options, parameters, attr_name) {
        var color, div, max, min, val;
        div = document.createElement("div");
        div.className = "renderedCell";
        min = parseFloat(parameters[0]);
        max = parseFloat(parameters[1]);
        val = Math.round(100 * ((parseFloat(object[attr_name])) - min) / (max - min));
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
        div.innerHTML = object[attr_name];
        return div;
      }
    }
  };
  module = {
    setup: function() {
      var attr_grid, domains_grid, statistics_grid;
      attr_grid = new grid.onDemand({
        store: internal.attr_store,
        columns: [
          {
            field: "name",
            label: "Attribute value"
          }
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
        var attribute;
        attribute = event.rows[0].data;
        internal.stats_store.setData([]);
        internal.basicStats(attribute);
        return topic.publish("request histogram", {
          attr: attribute.name,
          isDiscrete: internal.isDiscrete(internal.getBaseDomain(attribute)),
          bins: internal.getBaseParameters(attribute),
          callback: function(histogram) {
            var i, _i, _ref;
            internal.stats_store.put({
              id: "Histogram",
              value: ""
            });
            for (i = _i = 0, _ref = histogram.length - 1; 0 <= _ref ? _i <= _ref : _i >= _ref; i = 0 <= _ref ? ++_i : --_i) {
              internal.stats_store.put({
                id: this.bins[i],
                value: histogram[i]
              });
            }
            return registry.byId("statistics").refresh();
          }
        });
      });
      topic.subscribe("experiment loaded", function(input) {
        var attribute, columns;
        internal.attr_store.setData(input.attributes);
        internal.domains_store.setData(input.domains);
        columns = (function() {
          var _i, _len, _ref, _results;
          _ref = internal.attr_store.query({});
          _results = [];
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            attribute = _ref[_i];
            _results.push({
              field: attribute.name,
              label: attribute.name,
              renderCell: internal.partialRight(internal.partialRight(internal.cellRenderers[internal.getBaseDomain(attribute)], attribute.name), internal.getBaseParameters(attribute))
            });
          }
          return _results;
        })();
        topic.publish("provide columns info", columns);
        registry.byId("attributes").refresh();
        return registry.byId("domains").refresh();
      });
      return topic.subscribe("collect experiment data", function(collect) {
        var input;
        input = {
          attributes: internal.attr_store.query({}),
          domains: internal.domains_store.query({})
        };
        return collect(input);
      });
    }
  };
  return module;
});
