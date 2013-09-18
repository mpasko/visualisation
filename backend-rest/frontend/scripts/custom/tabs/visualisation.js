define(["dojo/topic", "dojo/store/Memory", "custom/visualisation/latex"], function(topic, Memory, latex) {
  var internal, module;
  internal = null;
  return module = {
    setup: function() {
      latex.setup();
      return topic.subscribe("visualise results", function(results) {
        return latex.renderMath("MathOutput", results);
      });
    }
  };
});
