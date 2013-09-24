define(["dojo/topic", "dojo/store/Memory", "custom/visualisation/latex"], function(topic, Memory, latex) {
  var internal, module;
  internal = {
    output_store: new Memory()
  };
  return module = {
    setup: function() {
      latex.setup();
      return topic.subscribe("visualise results", function(results) {
        internal.output_store.setData(results);
        return latex.renderMath("MathOutput", results);
      });
    }
  };
});
