define(["custom/utils/attributes"], function(attributeUtils) {
  var internal;
  internal = {
    partialRight: function(fn, args) {
      var aps;
      aps = Array.prototype.slice;
      args = aps.call(arguments, 1);
      return function() {
        return fn.apply(this, aps.call(arguments).concat(args));
      };
    },
    nominal: function(object, value, node, options, parameters, attr_name) {
      var div;
      div = document.createElement("div");
      div.className = "renderedCell";
      div.innerHTML = value;
      return div;
    },
    linear: function(object, value, node, options, parameters, attr_name) {
      var div;
      div = document.createElement("div");
      div.className = "renderedCell";
      div.innerHTML = value;
      return div;
    },
    continuous: function(object, value, node, options, parameters, attr_name) {
      var color, div, max, min, val;
      div = document.createElement("div");
      div.className = "renderedCell";
      min = parseFloat(parameters[0]);
      max = parseFloat(parameters[1]);
      val = Math.round(100 * ((parseFloat(value)) - min) / (max - min));
      if (val < 33) {
        color = "#3FFF00";
      } else if (val < 66) {
        color = "#FFD300";
      } else {
        color = "#ED1C24";
      }
      div.style.backgroundColor = color;
      div.style.width = val + "%";
      div.style.textAlign = "center";
      div.style.borderRadius = "15px";
      div.innerHTML = value;
      return div;
    }
  };
  return {
    get: function(attribute, domain_query) {
      var a, baseDomain, baseParameters;
      baseDomain = attributeUtils.getBaseDomain(attribute, domain_query);
      baseParameters = attributeUtils.getBaseParameters(attribute, domain_query);
      a = internal.partialRight(internal[baseDomain], attribute.name);
      return internal.partialRight(a, baseParameters);
    }
  };
});
