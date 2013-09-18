define(["dojo/dom", "dojo/aspect", "dojo/dom-construct", "dojo/_base/window", "dojo/topic", "dojo/store/Memory", "dijit/registry", "custom/file", "custom/grid", "custom/statistics"], function(dom, aspect, domConstruct, win, topic, Memory, registry, file, grid, stats) {
  var internal, module;
  internal = {
    createFileLoader: function() {
      domConstruct.create("input", {
        style: {
          display: "none"
        },
        type: "file",
        id: "load_files"
      }, win.body());
      return dom.byId("load_files").addEventListener('change', file.eventHandler, false);
    }
  };
  module = {
    setup: function() {
      var attr_grid, domains_grid, statistics_grid;
      internal.createFileLoader();
      attr_grid = new grid({
        columns: [
          {
            field: "id",
            label: "Attribute name"
          }, {
            field: "name",
            label: "Attribute value"
          }
        ]
      }, "attributes");
      domains_grid = new grid({
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
      statistics_grid = new grid({
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
        var attribute, stats_store;
        attribute = event.rows[0].data;
        stats_store = new Memory({
          data: [
            {
              id: "Domain",
              value: attribute.domain
            }
          ].concat(stats.computeStats(attribute))
        });
        registry.byId("statistics").set("store", stats_store);
        return registry.byId("statistics").refresh();
      });
      topic.subscribe("experiment loaded", function(input) {
        internal.attr_store = new Memory({
          data: input.attributes
        });
        registry.byId("attributes").set("store", internal.attr_store);
        registry.byId("attributes").refresh();
        internal.domains_store = new Memory({
          data: input.domains
        });
        registry.byId("domains").set("store", internal.domains_store);
        return registry.byId("domains").refresh();
      });
      return topic.subscribe("collect experiment data", function() {
        var input;
        console.log("attributes");
        input = {
          attributes: internal.attr_store.query({}),
          domains: internal.domains_store.query({})
        };
        return topic.publish("respond experiment data", input);
      });
    }
  };
  return module;
});
