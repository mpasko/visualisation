define(["dojo/dom", "dijit/registry", "dojo/store/Memory"], function(dom, registry, Memory) {
  var internal, module;
  internal = {};
  module = {
    updateTextbox: function(results) {
      var rawtextbox;
      rawtextbox = registry.byId("RawAq21");
      return rawtextbox.set("value", results);
    },
    setup: function() {
      return {};
    }
  };
  return module;
});
