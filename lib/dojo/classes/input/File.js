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
				return editor(
					{field : x.name, label:x.name}, 
					"text", "dblclick"
				);
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
			return this.json.runsGroup.runs.map(function(parent) { 
				return {
					id:parent.name, 
					type:"header",
					children : parent.runSpecificParameters.map(
						function(parameter) { 
							return {
								id:parameter.name, 
								value:parameter.value, 
								type:"parameter",
								children : []
							};
						})
					};
			});
	    }
	
    });
});