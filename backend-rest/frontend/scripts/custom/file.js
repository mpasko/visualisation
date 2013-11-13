define(["custom/backend"], function(backend) {
  return {
    eventHandler: function(evt) {
      var extension, f, reader;
      f = evt.target.files[0];
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
    }
  };
});
