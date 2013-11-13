define(["dojo/dom", "dojo/_base/window", "dojo/topic", "dojo/store/Memory", "dijit/registry", "custom/grid", "dgrid/editor", "dijit/form/TextBox", "custom/cellRenderers", "custom/utils"], function(dom, win, topic, Memory, registry, grid, editor, TextBox, cellRenderers, utils) {
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
    extractAttributeDescription: function(attribute) {
      return {
        domain: attribute.domain,
        baseDomain: this.getBaseDomain(attribute),
        parameters: this.getBaseParameters(attribute)
      };
    }
  };
  module = {
    setup: function() {
      var attr_grid, domains_grid, statistics_grid;
      attr_grid = new grid.onDemand({
        store: internal.attr_store,
        columns: [
          editor({
            field: "name",
            label: "Attribute value",
            autoSave: true
          }, TextBox, "dblclick")
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
        var addMinMax, addNominalValues, attribute, description;
        attribute = event.rows[0].data;
        internal.stats_store.setData([]);
        description = internal.extractAttributeDescription(attribute);
        internal.stats_store.put({
          id: "Domain",
          value: description.domain
        });
        internal.stats_store.put({
          id: "Base domain",
          value: description.baseDomain
        });
        addMinMax = function(params) {
          internal.stats_store.put({
            id: "Minimum",
            value: params[0]
          });
          return internal.stats_store.put({
            id: "Maximum",
            value: params[1]
          });
        };
        addNominalValues = function(params) {
          var i, _results;
          i = 0;
          _results = [];
          while (i < params.length) {
            internal.stats_store.put({
              id: i,
              value: params[i]
            });
            _results.push(i++);
          }
          return _results;
        };
        switch (description.baseDomain) {
          case "linear":
            addNominalValues(description.parameters);
            break;
          case "nominal":
            addNominalValues(description.parameters);
            break;
          case "continuous":
            addMinMax(description.parameters);
        }
        return registry.byId("statistics").refresh();
      });
      attr_grid.on("dgrid-datachange", function(event) {
        var attribute, attributes, columns, i;
        attributes = internal.attr_store.query({});
        i = 0;
        while (i < attributes.length) {
          if (attributes[i].name === event.oldValue) {
            attributes[i].name = event.value;
          }
          i++;
        }
        console.log(event);
        columns = (function() {
          var _i, _len, _results;
          _results = [];
          for (_i = 0, _len = attributes.length; _i < _len; _i++) {
            attribute = attributes[_i];
            _results.push({
              field: 'attribute' + (attributes.indexOf(attribute) + 1),
              label: attribute.name,
              autoSave: true,
              renderCell: utils.partialRight(utils.partialRight(cellRenderers[internal.getBaseDomain(attribute)], attribute.name), internal.getBaseParameters(attribute))
            });
          }
          return _results;
        })();
        return topic.publish("provide columns info", columns);
      });
      topic.subscribe("experiment loaded", function(input) {
        var attribute, attributes, columns;
        internal.attr_store.setData(input.attributes);
        internal.domains_store.setData(input.domains);
        attributes = internal.attr_store.query({});
        columns = (function() {
          var _i, _len, _results;
          _results = [];
          for (_i = 0, _len = attributes.length; _i < _len; _i++) {
            attribute = attributes[_i];
            _results.push({
              field: 'attribute' + (attributes.indexOf(attribute) + 1),
              label: attribute.name,
              autoSave: true,
              renderCell: utils.partialRight(utils.partialRight(cellRenderers[internal.getBaseDomain(attribute)], attribute.name), internal.getBaseParameters(attribute))
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
