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
      var attribute, attributes, selected_attributes, tmp, width, _i, _len, _results;
      attributes = stores.attr.query({});
      width = String(attributes.length).length;
      selected_attributes = stores.attr.query({
        selected: true
      });
      tmp = (function() {
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
      _results = [];
      for (_i = 0, _len = tmp.length; _i < _len; _i++) {
        attribute = tmp[_i];
        _results.push({
          field: 'attribute' + this.pad(attributes.indexOf(attribute) + 1, width),
          attribute: attribute
        });
      }
      return _results;
    },
    getNumericAttributes: function(attr, domains) {
      var a, attrs, s, width, _i, _len, _ref, _results;
      attrs = attr.query({});
      s = attr.query({
        selected: true
      });
      width = String(attrs.length).length;
      _results = [];
      for (_i = 0, _len = s.length; _i < _len; _i++) {
        a = s[_i];
        if ((_ref = this.getBaseDomain(a, domains.query({
          name: a.domain
        }))) === "integer" || _ref === "continuous") {
          _results.push('attribute' + this.pad(attrs.indexOf(a) + 1, width));
        }
      }
      return _results;
    },
    getCategories: function(attr, domains) {
      var a, attrs, i, tmp, _i, _len, _results;
      attrs = attr.query({});
      tmp = (function() {
        var _i, _len, _ref, _results;
        _results = [];
        for (_i = 0, _len = attrs.length; _i < _len; _i++) {
          a = attrs[_i];
          if ((_ref = this.getBaseDomain(a, domains.query({
            name: a.domain
          }))) === "integer" || _ref === "linear" || _ref === "nominal") {
            _results.push(a);
          }
        }
        return _results;
      }).call(this);
      i = 0;
      _results = [];
      for (_i = 0, _len = tmp.length; _i < _len; _i++) {
        a = tmp[_i];
        _results.push({
          id: i++,
          name: a.name
        });
      }
      return _results;
    },
    getUserToData: function(attr, domains, arr) {
      var a, attrs, obj, s, tmp, width, _i, _len;
      attrs = attr.query({});
      s = attr.query({
        selected: true
      });
      width = String(attrs.length).length;
      tmp = (function() {
        var _i, _len, _ref, _results;
        _results = [];
        for (_i = 0, _len = s.length; _i < _len; _i++) {
          a = s[_i];
          if (_ref = this.getBaseDomain(a, domains.query({
            name: a.domain
          })), __indexOf.call(arr, _ref) >= 0) {
            _results.push(a);
          }
        }
        return _results;
      }).call(this);
      obj = {};
      for (_i = 0, _len = tmp.length; _i < _len; _i++) {
        a = tmp[_i];
        obj[a.name] = 'attribute' + this.pad(attrs.indexOf(a) + 1, width);
      }
      return obj;
    },
    getDataToUser: function(attr, domains, arr) {
      var a, attrs, obj, s, tmp, width, _i, _len;
      attrs = attr.query({});
      s = attr.query({
        selected: true
      });
      width = String(attrs.length).length;
      tmp = (function() {
        var _i, _len, _ref, _results;
        _results = [];
        for (_i = 0, _len = s.length; _i < _len; _i++) {
          a = s[_i];
          if (_ref = this.getBaseDomain(a, domains.query({
            name: a.domain
          })), __indexOf.call(arr, _ref) >= 0) {
            _results.push(a);
          }
        }
        return _results;
      }).call(this);
      obj = {};
      for (_i = 0, _len = tmp.length; _i < _len; _i++) {
        a = tmp[_i];
        obj['attribute' + this.pad(attrs.indexOf(a) + 1, width)] = a.name;
      }
      return obj;
    },
    getDomains: function(attr, domains) {
      var a, attrs, extents, obj, s, tmp, width, _i, _len;
      attrs = attr.query({});
      s = attr.query({
        selected: true
      });
      width = String(attrs.length).length;
      tmp = (function() {
        var _i, _len, _ref, _results;
        _results = [];
        for (_i = 0, _len = s.length; _i < _len; _i++) {
          a = s[_i];
          if ((_ref = this.getBaseDomain(a, domains.query({
            name: a.domain
          }))) === "integer" || _ref === "continuous") {
            _results.push(a);
          }
        }
        return _results;
      }).call(this);
      obj = {};
      for (_i = 0, _len = tmp.length; _i < _len; _i++) {
        a = tmp[_i];
        extents = this.getBaseParameters(a, domains.query({
          name: a.domain
        }));
        obj['attribute' + this.pad(attrs.indexOf(a) + 1, width)] = [parseFloat(extents[0]), parseFloat(extents[1])];
      }
      return obj;
    },
    cross: function(a, b) {
      var c, i, j;
      c = [];
      i = 0;
      while (i < a.length) {
        j = 0;
        while (j < b.length) {
          if (i >= j) {
            c.push({
              i: i,
              j: j,
              x: a[i],
              y: b[j]
            });
          }
          j++;
        }
        i++;
      }
      return c;
    }
  };
});
