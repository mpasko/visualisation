define(["custom/backend", "dojo/dom", "dojo/dom-construct", "dojo/_base/window", "dijit/registry"], function(backend, dom, domConstruct, win, registry) {
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
            return backend.convert.AQ21(file_content);
          } else if (extension.match(/\.(csv)$/)) {
            return backend.convert.CSV(file_content);
          } else {
            return humane.log("Not a valid extension");
          }
        };
        return converting(e.target.result);
      };
      return reader.readAsText(f);
    },
    eventHandler: function(evt) {
      return internal.loadFile(evt.target.files[0]);
    }
  };
  module = {
    createFileLoader: function() {
      domConstruct.create("input", {
        style: {
          display: "none"
        },
        type: "file",
        id: "load_files"
      }, win.body());
      dom.byId("load_files").addEventListener('change', internal.eventHandler, false);
      return registry.byId("file_button").on('click', function() {
        return dom.byId("load_files").click();
      });
    }
  };
  return module;
});
