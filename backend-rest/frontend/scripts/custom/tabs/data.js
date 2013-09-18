define(["dojo/dom-construct", "dojo/topic", "dojo/store/Memory", "dijit/registry", "dijit/form/TextBox", "dgrid/editor", "custom/grid"], function(domConstruct, topic, Memory, registry, TextBox, editor, grid) {
  var internal, module;
  internal = {};
  module = {
    setup: function() {
      topic.subscribe("experiment loaded", function(input) {
        var attribute, events_grid;
        internal.events_store = new Memory({
          data: input.events
        });
        registry.byId("events").destroyDescendants(false);
        domConstruct.create("div", {
          id: "raw_data",
          style: "height : 100%;"
        }, "events");
        events_grid = new grid({
          store: internal.events_store,
          columns: (function() {
            var _i, _len, _ref, _results;
            _ref = input.attributes;
            _results = [];
            for (_i = 0, _len = _ref.length; _i < _len; _i++) {
              attribute = _ref[_i];
              _results.push(editor({
                field: attribute.name,
                label: attribute.name,
                autoSave: true
              }, TextBox, "click"));
            }
            return _results;
          })()
        }, "raw_data");
        return events_grid.refresh();
      });
      return topic.subscribe("collect experiment data", function() {
        var input;
        console.log("data");
        input = {
          events: internal.events_store.query({})
        };
        return topic.publish("respond experiment data", input);
      });
    }
  };
  return module;
});
