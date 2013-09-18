define([], function() {
  var internal, module;
  internal = {
    getRawDomain: function(item) {
      var _ref;
      if ((_ref = item.domain) === "linear" || _ref === "nominal" || _ref === "continuous") {
        return item.domain;
      } else {
        return domainsStore.query({
          name: item.domain
        })[0].domain;
      }
    },
    getRawParameters: function(item) {
      var parameters, _ref;
      if ((_ref = item.domain) === "linear" || _ref === "nominal" || _ref === "continuous") {
        parameters = item.parameters;
      } else {
        parameters = domainsStore.query({
          name: item.domain
        })[0].parameters;
      }
      return /([^{}]+)/.exec(parameters)[1].split(",");
    },
    nominal: function(name, parameters) {
      return [
        {
          id: "Item counts",
          value: ""
        }
      ].concat(parameters.map(function(parameter) {
        var query;
        query = {};
        query[name] = parameter.trim();
        query.value = eventsStore.query(query).total;
        query.id = parameter.trim();
        return query;
      }));
    },
    linear: function(name, parameters) {
      return this.nominal(name, parameters);
    },
    continuous: function(name, parameters) {
      return [
        {
          id: "Values range",
          value: ""
        }, {
          id: "min",
          value: parseFloat(parameters[0])
        }, {
          id: "max",
          value: parseFloat(parameters[1])
        }
      ];
    }
  };
  module = {
    computeStats: function(item) {
      return internal[internal.getRawDomain(item)](item.name, internal.getRawParameters(item));
    }
  };
  return module;
});
