window.define([], function() {
    return {
        getRawDomain : function (item) {
            if (["linear", "nominal", "continuous"].indexOf(item.domain) > -1) {
                return item.domain;
            } else {
                return domainsStore.query({name : item.domain})[0].domain;
            }
        },
        getRawParameters : function (item) {
            var parameters = null;
            if (["linear", "nominal", "continuous"].indexOf(item.domain) > -1) {
                parameters =  item.parameters;
            } else {
                parameters = domainsStore.query({name : item.domain})[0].parameters;
            }
            
            return parameters.replace("{", "").replace("}", "").split(",");
        },
        computeStats : function(item) {
            return this[this.getRawDomain(item)](item.name, this.getRawParameters(item));
        },
        nominal : function(name, parameters) {
                return [{ id : "Item counts", value : "" }].concat(
                    parameters.map(function(parameter) {
                        var query = {};
                        query[name] = parameter.trim();
                        query.value = eventsStore.query(query).total;
                        delete query.name;
                        query.id = parameter.trim();
                        
                        return query;
                }));
        },
        linear : function(name, parameters) {
            return this.nominal(name, parameters);
        },
        continuous : function(name, parameters) {
            var dataset = eventsStore.query({}).map(function(x) {
                return parseFloat(x[name]);
            });
            
            var min = parseFloat(parameters[0]), max = parseFloat(parameters[1]);
            return [{ id : "Values range", value : "" }].concat(
                [{  id : "min", value : min }, { id : "max", value : max }]);
        }
    };
});
