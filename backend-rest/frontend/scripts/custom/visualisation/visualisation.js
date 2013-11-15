define(["dojo/topic", "dojo/store/Memory", "custom/visualisation/latex", "custom/visualisation/raw"], function(topic, Memory, latex, raw) {
  var internal, module;
  internal = {
    output_store: new Memory()
  };
  return module = {
    visualiseResults: function(results) {
      internal.output_store.setData(results["outputHypotheses"]);
      raw.updateTextbox(results["raw_aq21"]);
      return latex.renderMath("MathOutput", results["outputHypotheses"]);
    },
    setup: function() {
      latex.setup();
      return raw.setup();
    }
  };
});
