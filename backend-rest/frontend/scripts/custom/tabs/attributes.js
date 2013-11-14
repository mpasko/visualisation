define(["dojo/dom", "dojo/_base/window", "dojo/topic", "dojo/store/Memory", "dijit/registry", "custom/grid", "dgrid/editor", "dijit/form/TextBox", "custom/cellRenderers", "custom/utils/attributes"], function(dom, win, topic, Memory, registry, grid, editor, TextBox, cellRenderers, utils) {
  var internal, module;
  internal = {
    stats_store: new Memory(),
    attr_store: new Memory(),
    domains_store: new Memory()
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
      attr_grid.on("dgrid-datachange", function(event) {
        var attribute, attributes, columns;
        attribute = internal.attr_store.query({
          name: event.oldValue
        })[0];
        attribute.name = event.value;
        internal.attr_store.put(attribute);
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
              renderCell: cellRenderers.get(attribute, internal.domains_store.query({
                name: attribute.domain
              }))
            });
          }
          return _results;
        })();
        return topic.publish("provide columns info for data tab", columns);
      });
      topic.subscribe("experiment loaded from backend", function(input) {
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
              renderCell: cellRenderers.get(attribute, internal.domains_store.query({
                name: attribute.domain
              }))
            });
          }
          return _results;
        })();
        topic.subscribe("provide data for scatter plot", function(data) {});
        topic.publish("provide columns info for data tab", columns);
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
