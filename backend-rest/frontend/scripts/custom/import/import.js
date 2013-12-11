define(["dijit/registry", "custom/import/file"], function(registry, file) {
  var internal, module;
  internal = {};
  module = {
    textProvided: function(text) {},
    setup: function() {
      return file.createFileLoader();
    }
  };
  return module;
});
