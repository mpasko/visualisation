define(["dojo/dom-construct", "dojo/topic", "dojo/store/Memory", "dijit/registry", "dijit/form/TextBox", "dgrid/editor", "custom/grid"], function(domConstruct, topic, Memory, registry, TextBox, editor, grid) {
  var internal, module;
  internal = {
    events_store: new Memory()
  };
  module = {
    setup: function() {
      topic.subscribe("experiment loaded", function(input) {
        return internal.events_store.setData(input.events);
      });
      topic.subscribe("provide columns info", function(columns) {
        var column, events_grid;
        registry.byId("events").destroyDescendants(false);
        domConstruct.create("div", {
          id: "raw_data",
          style: "height : 100%;"
        }, "events");
        events_grid = new grid({
          store: internal.events_store,
          columns: (function() {
            var _i, _len, _results;
            _results = [];
            for (_i = 0, _len = columns.length; _i < _len; _i++) {
              column = columns[_i];
              _results.push(editor(column, TextBox, "click"));
            }
            return _results;
          })()
        }, "raw_data");
        return events_grid.refresh();
      });
      topic.subscribe("collect experiment data", function(collect) {
        var input;
        input = {
          events: internal.events_store.query({})
        };
        return collect(input);
      });
      return topic.subscribe("request histogram", function(params) {
        if (params.isDiscrete) {
          return params.callback(params.bins.map(function(name) {
            var query;
            query = {};
            query[params.attr] = name;
            return internal.events_store.query(query).total;
          }));
        }
      });
    }
  };
  return module;
});
