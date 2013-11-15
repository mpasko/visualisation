define(["custom/backend", "dojo/dom", "dojo/dom-construct", "dojo/_base/window"], function(backend, dom, domConstruct, win) {
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
    },
    handleFileSelect: function(evt) {
      evt.stopPropagation();
      evt.preventDefault();
      return internal.loadFile(evt.dataTransfer.files[0]);
    },
    handleDragOver: function(evt) {
      evt.stopPropagation();
      evt.preventDefault();
      return evt.dataTransfer.dropEffect = 'copy';
    }
  };
  module = {
    createFileLoader: function() {
      var dropZone, tmp;
      domConstruct.create("input", {
        style: {
          display: "none"
        },
        type: "file",
        id: "load_files"
      }, win.body());
      dom.byId("load_files").addEventListener('change', internal.eventHandler, false);
      dropZone = dom.byId('drop_zone');
      dropZone.addEventListener('dragover', internal.handleDragOver, false);
      dropZone.addEventListener('drop', internal.handleFileSelect, false);
      tmp = function() {
        return dom.byId("load_files").click();
      };
      return dropZone.addEventListener('click', tmp, false);
    }
  };
  return module;
});
