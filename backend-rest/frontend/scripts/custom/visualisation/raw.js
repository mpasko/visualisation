define(["dojo/dom", "dijit/registry", "dojo/store/Memory"], function(dom, registry, Memory) {
  var internal, module;
  internal = {};
  module = {
    update: function(results) {
      var rawtextbox;
      rawtextbox = registry.byId("RawAq21");
      return rawtextbox.set("value", results["raw_aq21"]);
    },
    setup: function() {
      return {};
    }
  };
  return module;
});
