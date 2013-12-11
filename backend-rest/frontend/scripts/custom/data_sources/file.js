define(["dojo/dom-construct", "dojo/_base/window", "dijit/registry", "dojo/topic"], function(domConstruct, win, registry, topic) {
  var internal, module;
  internal = {
    loadFile: function(f) {
      var extension, reader;
      extension = f.fileName != null ? f.fileName : f.name;
      reader = new FileReader();
      reader.onload = function(e) {
        var converting;
        converting = function(file_content) {
          if (extension.match(/\.(aq21|AQ21|a21|q21)$/)) {
            return topic.publish("convert AQ21", file_content);
          } else if (extension.match(/\.(csv)$/)) {
            return topic.publish("convert CSV", file_content);
          } else {
            return humane.log("Not a valid extension");
          }
        };
        return converting(e.target.result);
      };
      return reader.readAsText(f);
    }
  };
  module = {
    createFileLoader: function() {
      var loader;
      loader = domConstruct.create("input", {
        style: {
          display: "none"
        },
        type: "file",
        id: "load_files"
      }, win.body());
      loader.addEventListener('change', function(evt) {
        return internal.loadFile(evt.target.files[0]);
      }, false);
      return registry.byId("file_button").on('click', function() {
        return loader.click();
      });
    }
  };
  return module;
});
