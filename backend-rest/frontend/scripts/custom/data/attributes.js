var __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

define([], function() {
  return {
    hasDefaultDomain: function(attribute) {
      var _ref;
      return (_ref = attribute.domain) === "linear" || _ref === "nominal" || _ref === "integer" || _ref === "continuous";
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
    },
    pad: function(n, width) {
      var n_str;
      n_str = String(n);
      while (n_str.length !== width) {
        n_str = '0' + n_str;
      }
      return n_str;
    },
    getMapping: function(stores, arr) {
      var attribute, attributes, discretes, selected_attributes, width, _i, _len, _results;
      attributes = stores.attr.query({});
      selected_attributes = stores.attr.query({
        selected: true
      });
      discretes = (function() {
        var _i, _len, _ref, _results;
        _results = [];
        for (_i = 0, _len = selected_attributes.length; _i < _len; _i++) {
          attribute = selected_attributes[_i];
          if (_ref = this.getBaseDomain(attribute, stores.domains.query({
            name: attribute.domain
          })), __indexOf.call(arr, _ref) >= 0) {
            _results.push(attribute);
          }
        }
        return _results;
      }).call(this);
      width = String(attributes.length).length;
      _results = [];
      for (_i = 0, _len = discretes.length; _i < _len; _i++) {
        attribute = discretes[_i];
        _results.push({
          field: 'attribute' + this.pad(attributes.indexOf(attribute) + 1, width),
          parameters: this.getBaseParameters(attribute, stores.domains.query({
            name: attribute.domain
          })),
          attribute: attribute
        });
      }
      return _results;
    },
    getDiscreteMapping: function(stores, arr) {
      var attribute, attributes, discretes, selected_attributes, width, _i, _len, _results;
      attributes = stores.attr.query({});
      selected_attributes = stores.attr.query({});
      discretes = (function() {
        var _i, _len, _ref, _results;
        _results = [];
        for (_i = 0, _len = selected_attributes.length; _i < _len; _i++) {
          attribute = selected_attributes[_i];
          if (_ref = this.getBaseDomain(attribute, stores.domains.query({
            name: attribute.domain
          })), __indexOf.call(arr, _ref) >= 0) {
            _results.push(attribute);
          }
        }
        return _results;
      }).call(this);
      width = String(attributes.length).length;
      _results = [];
      for (_i = 0, _len = discretes.length; _i < _len; _i++) {
        attribute = discretes[_i];
        _results.push({
          field: 'attribute' + this.pad(attributes.indexOf(attribute) + 1, width),
          parameters: this.getBaseParameters(attribute, stores.domains.query({
            name: attribute.domain
          })),
          attribute: attribute
        });
      }
      return _results;
    }
  };
});
