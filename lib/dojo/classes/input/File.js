define(["dojo/_base/declare", "dojo/store/Memory", "dojo/store/Observable", "dgrid/editor","dgrid/tree", 
"dojo/_base/Deferred","dojo/store/util/QueryResults"], 
function (declare, Memory,Observable, editor,tree, Deferred,QueryResults) 
{
		// Create a new class named "mynamespace.MyClass"
		return declare("classes.input.File", null, {
		// The constructor
	    constructor: function(args){
	        declare.safeMixin(this,args);
	        this.json = JSON.parse(this.text);
	    }, 
	    getAttributes : function() {
	    	var i = 0;
			return this.json.attributes.map(function(x) { 
				x.id = i++;
				return x;
			});
	    },
	    getColumnsName : function() {
			return this.json.attributes.map(function(x) { 
				return 	{field : x.name, label:x.name};
			});
	    },
	    getData : function() {
		    var zip = function(arrays) {
			    return arrays[0].map(function(_,i){
			        return arrays.map(function(array){return array[i];});
			    });
			};
			
			var names = this.getColumnsName();
			var events = this.json.events;
			var i = 0;
			return events.map(
				function(item) {
					var temp = zip([item.values, names]);
					var out = {};
					temp.forEach(function (item) { out[item[1].field] = item[0];});
					out.id = i++;
					return out;
			});
	    },
	    getConfiguration : function() {
			var parameters = [];
			var id = 0;
			this.json.runsGroup.runs.forEach(function(parent) { 
				parent.runSpecificParameters.forEach(
						function(parameter) {
							parameters.push({"parent": parent.name, name:parameter.name, value:parameter.value, id :id++});
						});
					});
					
			this.json.runsGroup.globalLearningParameters.forEach(
						function(parameter) { 
							parameters.push({parent : "global", name:parameter.name, value:parameter.value, id :id++});
						});
			
			return parameters;
	    },
	    getConfigurationNames : function() {
			return this.json.runsGroup.runs.map(function(parent) { return parent.name; });

	
		}
    });
});