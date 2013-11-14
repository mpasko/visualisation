define(["custom/backend", "dojo/dom", "dojo/dom-construct", "dojo/_base/window", "dojo/topic"], function(backend, dom, domConstruct, win, topic) {
  return {
    eventHandler: function(evt) {
      var extension, f, reader;
      f = evt.target.files[0];
      extension = f.fileName != null ? f.fileName : f.name;
      reader = new FileReader();
      reader.onload = function(e) {
        var converting;
        converting = function(file_content) {
          topic.publish("experiment raw text loaded", file_content);
          if (extension.match(/\.(aq21|AQ21|a21|q21)$/)) {
            return backend.convertAQ21(file_content);
          } else if (extension.match(/\.(csv)$/)) {
            return backend.convertCSV(file_content);
          } else {
            return humane.log("Not a valid extension");
          }
        };
        return converting(e.target.result);
      };
      return reader.readAsText(f);
    },
    handleDragOver: function(evt) {
      evt.stopPropagation();
      evt.preventDefault();
      return evt.dataTransfer.dropEffect = 'copy';
    },
    handleFileSelect: function(evt) {
      var extension, f, reader;
      evt.stopPropagation();
      evt.preventDefault();
      f = evt.dataTransfer.files[0];
      extension = f.fileName != null ? f.fileName : f.name;
      console.log(URL.createObjectURL(f));
      reader = new FileReader();
      reader.onload = function(e) {
        var converting;
        converting = function(file_content) {
          if (extension.match(/\.(aq21|AQ21|a21|q21)$/)) {
            return backend.convertAQ21(file_content);
          } else {
            return humane.log("Not a valid AQ21 extension");
          }
        };
        return converting(e.target.result);
      };
      return reader.readAsText(f);
    },
    createFileLoader: function() {
      var dropZone, tmp;
      domConstruct.create("input", {
        style: {
          display: "none"
        },
        type: "file",
        id: "load_files"
      }, win.body());
      dom.byId("load_files").addEventListener('change', this.eventHandler, false);
      dropZone = dom.byId('drop_zone');
      dropZone.addEventListener('dragover', this.handleDragOver, false);
      dropZone.addEventListener('drop', this.handleFileSelect, false);
      tmp = function() {
        return dom.byId("load_files").click();
      };
      return dropZone.addEventListener('click', tmp, false);
    }
  };
});
