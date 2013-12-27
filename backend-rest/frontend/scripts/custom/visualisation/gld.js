define(["dojo/dom", "dijit/registry", "dojo/topic"], function(dom, registry, topic) {
  var internal, module;
  internal = {
    results: null,
    visualise_gld: function(gld) {
      var c, ctx, i, item, j, _i, _len, _ref, _results;
      console.log(gld);
      c = dom.byId("myCanvas");
      c.height = 3 * gld.width;
      c.width = 3 * gld.width;
      ctx = c.getContext("2d");
      ctx.fillStyle = "#FF0000";
      _ref = gld.values["[class=iris-virginica]"].exists_in;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        item = _ref[_i];
        i = item % gld.width;
        j = Math.floor(item / gld.width);
        console.log(i, j);
        _results.push(ctx.fillRect(3 * i, 3 * j, 3, 3));
      }
      return _results;
    }
  };
  module = {
    setup: function() {
      topic.subscribe("visualise gld", internal.visualise_gld);
      return registry.byId("load_gld").on("click", function() {
        return topic.publish("create gld diagram", {
          data: internal.results,
          ratio_importance: parseFloat(registry.byId("gldParameter").value)
        });
      });
    },
    update: function(results) {
      return internal.results = results;
    }
  };
  return module;
});
