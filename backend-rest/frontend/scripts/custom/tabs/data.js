define(["dojo/dom-construct", "dojo/topic", "dojo/store/Memory", "dijit/registry", "dijit/form/TextBox", "dgrid/editor", "custom/grid"], function(domConstruct, topic, Memory, registry, TextBox, editor, grid) {
  var internal, module;
  internal = {
    events_store: new Memory()
  };
  module = {
    setup: function() {
      topic.subscribe("experiment loaded from backend", function(input) {
        return internal.events_store.setData(input.events);
      });
      topic.subscribe("provide columns info for data tab", function(columns) {
        var column, events_grid;
        registry.byId("events").destroyDescendants(false);
        domConstruct.create("div", {
          id: "raw_data",
          style: "height : 100%;"
        }, "events");
        events_grid = new grid.paginated({
          store: internal.events_store,
          columns: (function() {
            var _i, _len, _results;
            _results = [];
            for (_i = 0, _len = columns.length; _i < _len; _i++) {
              column = columns[_i];
              _results.push(editor(column, "text", "click"));
            }
            return _results;
          })(),
          pagingLinks: 3,
          firstLastArrows: true,
          pageSizeOptions: [20, 50, 100]
        }, "raw_data");
        events_grid.refresh();
        return topic.publish("provide data for scatter plot", internal.events_store.query({}));
      });
      return topic.subscribe("collect experiment data", function(collect) {
        var input;
        input = {
          events: internal.events_store.query({})
        };
        return collect(input);
      });
    }
  };
  return module;
});
