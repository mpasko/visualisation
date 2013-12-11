define(["dojo/topic", "dojo/store/Memory", "custom/visualisation/latex", "custom/visualisation/raw"], function(topic, Memory, latex, raw) {
  var internal, module;
  internal = {
    stores: {
      output: new Memory()
    },
    visualisations: [raw, latex]
  };
  return module = {
    update: function(results) {
      var item, _i, _len, _ref, _results;
      internal.stores.output.setData(results["outputHypotheses"]);
      _ref = internal.visualisations;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        item = _ref[_i];
        _results.push(item.update(results));
      }
      return _results;
    },
    setup: function() {
      latex.setup();
      return raw.setup();
    }
  };
});
