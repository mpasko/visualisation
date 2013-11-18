define(["dojo/query", "dojo/dom-style", "dojo/_base/fx", "dojo/fx"], function(query, domStyle, fx, coreFx) {
  var internal, module;
  internal = null;
  module = {
    play: function() {
      var chain, name;
      chain = coreFx.chain((function() {
        var _i, _len, _ref, _results;
        _ref = ((function() {
          var _j, _len, _ref, _results1;
          _ref = query(".splash");
          _results1 = [];
          for (_j = 0, _len = _ref.length; _j < _len; _j++) {
            name = _ref[_j];
            _results1.push(name);
          }
          return _results1;
        })()).reverse();
        _results = [];
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          name = _ref[_i];
          _results.push(fx.animateProperty({
            node: name,
            properties: {
              opacity: {
                start: 1,
                end: 0
              }
            },
            duration: 500
          }));
        }
        return _results;
      })());
      chain.onEnd = function() {
        var _i, _len, _ref, _results;
        _ref = query(".splash");
        _results = [];
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          name = _ref[_i];
          _results.push(domStyle.set(name, "display", "none"));
        }
        return _results;
      };
      return chain.play();
    }
  };
  return module;
});
