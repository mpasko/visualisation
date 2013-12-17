define(["dojo/dom", "dijit/registry"], function(dom, registry) {
  var internal, module;
  internal = {
    isEmpty: function(str) {
      return !str || 0 === str.length;
    },
    dict: {
      ">": ">",
      "<": "<",
      ">=": "\\geq",
      "<=": "\\leq",
      "=": "="
    },
    renderSelector: function(selector) {
      var index, output;
      if (!this.isEmpty(selector.range_begin) && !this.isEmpty(selector.range_end)) {
        return ["(", selector.name, "\\in\\langle", selector.range_begin, ",\\ ", selector.range_end, "\\rangle", ")"].join(" ");
      } else if (!this.isEmpty(selector.set_elements)) {
        index = 0;
        output = ["(", selector.name, "\\in\\{"];
        selector.set_elements.forEach(function(element) {
          output.push(element);
          ++index;
          if (index !== selector.set_elements.length) {
            return output.push(",\\ ");
          }
        });
        output.push("\\})");
        return output.join(" ");
      } else {
        return ["(", selector.name, this.dict[selector.comparator], selector.value, ")"].join(" ");
      }
    },
    renderRule: function(rule, consequent) {
      var antecedent;
      antecedent = ((function() {
        var _i, _len, _ref, _results;
        _ref = rule.selectors;
        _results = [];
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          rule = _ref[_i];
          _results.push(this.renderSelector(rule));
        }
        return _results;
      }).call(this)).join(" \\land ");
      return ["$$", antecedent, "\\implies", consequent, "$$"].join("\n");
    },
    renderHypotheses: function(hyp) {
      var consequent, rule;
      consequent = this.renderSelector(hyp.classes[0]);
      return ((function() {
        var _i, _len, _ref, _results;
        _ref = hyp.rules;
        _results = [];
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          rule = _ref[_i];
          _results.push(this.renderRule(rule, consequent));
        }
        return _results;
      }).call(this)).join("\n");
    },
    updateMath: function(id, TeX) {
      dom.byId(id).innerHTML = TeX;
      return MathJax.Hub.Queue(["Typeset", MathJax.Hub, id]);
    },
    convertToLaTex: function(hypotheses) {
      var hyp;
      return ((function() {
        var _i, _len, _results;
        _results = [];
        for (_i = 0, _len = hypotheses.length; _i < _len; _i++) {
          hyp = hypotheses[_i];
          _results.push(internal.renderHypotheses(hyp));
        }
        return _results;
      })()).join(" ");
    }
  };
  module = {
    setup: function() {
      return MathJax.Hub.Config({
        tex2jax: {
          inlineMath: [["$$", "$$"]]
        },
        imageFont: null
      });
    },
    update: function(results) {
      var hypotheses, text, textbox;
      hypotheses = results["outputHypotheses"];
      if (hypotheses.length === 0) {
        return;
      }
      console.log("The server returned: ", hypotheses);
      text = internal.convertToLaTex(hypotheses);
      internal.updateMath("MathOutput", text);
      textbox = registry.byId("LatexCode");
      return textbox.set("value", text);
    }
  };
  return module;
});
