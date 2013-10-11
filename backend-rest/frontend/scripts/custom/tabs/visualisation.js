define(["dojo/topic", "dojo/store/Memory", "custom/visualisation/latex", "custom/visualisation/raw"], function(topic, Memory, latex, raw) {
  var internal, module;
  internal = {
    output_store: new Memory()
  };
  return module = {
    setup: function() {
      latex.setup();
      return topic.subscribe("visualise results", function(results) {
        internal.output_store.setData(results);
        latex.renderMath("MathOutput", results);
        return raw.setup();
      });
    }
  };
});
