define([], function() {
  return {
    hasDefaultDomain: function(attribute) {
      var _ref;
      return (_ref = attribute.domain) === "linear" || _ref === "nominal" || _ref === "continuous";
    },
    getBaseDomain: function(attribute, domain_query) {
      if (this.hasDefaultDomain(attribute)) {
        return attribute.domain;
      } else {
        return domain_query[0].domain;
      }
    },
    getBaseParameters: function(attribute, domain_query) {
      var parameter, parameters, tmp, _i, _len, _results;
      if (this.hasDefaultDomain(attribute)) {
        parameters = attribute.parameters;
      } else {
        parameters = domain_query[0].parameters;
      }
      tmp = (function() {
        var _i, _len, _ref, _results;
        _ref = /([^{}]+)/.exec(parameters)[1].split(" ");
        _results = [];
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          parameter = _ref[_i];
          _results.push(parameter.trim());
        }
        return _results;
      })();
      _results = [];
      for (_i = 0, _len = tmp.length; _i < _len; _i++) {
        parameter = tmp[_i];
        _results.push(parameter.slice(-1) === "," ? parameter.slice(0, -1) : parameter);
      }
      return _results;
    },
    extractAttributeDescription: function(attribute, domain_query) {
      return {
        domain: attribute.domain,
        baseDomain: this.getBaseDomain(attribute, domain_query),
        parameters: this.getBaseParameters(attribute, domain_query)
      };
    },
    getContinuousValues: function(params) {
      return [
        {
          id: "Minimum",
          value: params[0]
        }, {
          id: "Maximum",
          value: params[1]
        }
      ];
    },
    getDiscreteValues: function(params) {
      var arr, i;
      arr = [];
      i = 0;
      while (i < params.length) {
        arr.push({
          id: i,
          value: params[i]
        });
        i++;
      }
      return arr;
    }
  };
});
