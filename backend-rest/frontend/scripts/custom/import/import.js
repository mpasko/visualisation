define(["dijit/registry", "custom/import/file"], function(registry, file) {
  var internal, module;
  internal = {};
  module = {
    textProvided: function(text) {
      registry.byId("loaded_text").set('value', text.split("\n").slice(0, 31).join("\n"));
      return registry.byId("accordion").selectChild(registry.byId("raw_aq21_text"), true);
    },
    setup: function() {
      return file.createFileLoader();
    }
  };
  return module;
});
