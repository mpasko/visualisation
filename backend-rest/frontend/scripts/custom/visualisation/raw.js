define(["dojo/dom", "dijit/registry", "dojo/topic", "dojo/store/Memory"], function(dom, registry, topic, Memory) {
  var internal, module;
  internal = {
    output_store: new Memory()
  };
  module = {
    setup: function() {
      console.log("Raw module init");
      return topic.subscribe("raw format", function(results) {
        var rawtextbox;
        internal.output_store.setData(results);
        console.log("The server returned raw format: ", results);
        rawtextbox = registry.byId("RawAq21");
        return rawtextbox.set("value", results);
      });
    }
  };
  return module;
});
