(function() {
  define(["dojo/topic", "dijit/registry", "custom/file"], function(topic, registry, file) {
    var internal, module;
    internal = {
      textProvided: function(text) {
        registry.byId("loaded_text").set('value', text.split("\n").slice(0, 31).join("\n"));
        return registry.byId("accordion").selectChild(registry.byId("raw_aq21_text"), true);
      }
    };
    module = {
      setup: function() {
        file.createFileLoader();
        return topic.subscribe("experiment raw text loaded", internal.textProvided);
      }
    };
    return module;
  });

}).call(this);
