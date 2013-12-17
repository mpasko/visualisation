define(["dojo/dom", "dijit/registry", "dojo/topic"], function(dom, registry, topic) {
  var internal, module;
  internal = {
    results: null
  };
  module = {
    setup: function() {
      return registry.byId("load_gld").on("click", function() {
        return topic.publish("create gld diagram", {
          data: internal.results,
          ratio_importance: parseFloat(registry.byId("gldParameter").value)
        });
      });
    },
    update: function(results) {
      internal.results = results;
      return console.log("aaaa");
    }
  };
  return module;
});
