/* global define window */
/* global define console */
/* global define attributesStore */
/* global define eventsStore */
/* global define domainsStore */

window.define([], function() {
	return {
		computeStats : function(item) {
			var parameters = null;
			var domain = null;
			if (["linear", "nominal", "continuous"].indexOf(item.domain) > -1) {
				parameters = item.parameters;
				domain = item.domain;
			} else {
				var queryResult = domainsStore.query({
							name : item.domain
						})[0];
				parameters = queryResult.parameters;
				domain = queryResult.domain;
			}

			parameters = parameters.replace("{", "").replace("}", "")
					.split(",");
			return this[domain](item.name, parameters);
		},
		nominal : function(name, parameters) {

			return [{
						id : "Item counts",
						value : ""
					}].concat(parameters.map(function(parameter) {
						var query = {};
						query[name] = parameter.trim();
						query.value = eventsStore.query(query).total;
						delete query[name]
						query.id = parameter.trim();
						return query;
					}));
		},
		linear : function(name, parameters) {

			return [{
						id : "Item counts",
						value : ""
					}].concat(parameters.map(function(parameter) {
						var query = {};
						query[name] = parameter.trim();
						query.value = eventsStore.query(query).total;
						delete query[name]
						query.id = parameter.trim();
						return query;
					}));
		},
		continuous : function(name, parameters) {
			var dataset = eventsStore.query({}).map(function(x) {
						return parseFloat(x[name]);
					});
			var min = parseFloat(parameters[0]), max = parseFloat(parameters[1]);
			return [{
						id : "Values range",
						value : ""
					}].concat([{
						id : "min",
						value : min
					}, {
						id : "max",
						value : max
					}]);
		}
	};
});
