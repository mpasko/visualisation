define(["dojo/dom", "dojo/dom-construct", "dojo/_base/window", "dojo/topic", "dijit/registry", "custom/file", "dojo/on"], function(dom, domConstruct, win, topic, registry, file, dojo_on) {
  var internal, module;
  internal = {
    createFileLoader: function() {
      var dropZone, tmp;
      domConstruct.create("input", {
        style: {
          display: "none"
        },
        type: "file",
        id: "load_files"
      }, win.body());
      dom.byId("load_files").addEventListener('change', file.eventHandler, false);
      dropZone = dom.byId('drop_zone');
      dropZone.addEventListener('dragover', file.handleDragOver, false);
      dropZone.addEventListener('drop', file.handleFileSelect, false);
      tmp = function() {
        return dom.byId("load_files").click();
      };
      return dropZone.addEventListener('click', tmp, false);
    }
  };
  module = {
    setup: function() {
      internal.createFileLoader();
      return topic.subscribe("experiment raw text", function(text) {
        registry.byId("loaded_text").set('value', text);
        return registry.byId("accordion").selectChild(registry.byId("raw_aq21_text"), true);
      });
    }
  };
  return module;
});
