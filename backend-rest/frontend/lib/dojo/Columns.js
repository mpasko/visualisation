/*global define window*/
window.define(
	[
		"dgrid/editor"
	],
	function(editor) {
		return {
            runs : { name : {label: "Number"}, value: {label: "Value"} },
            runNames : { id : {label: "Run"}, value: editor({name: "CheckBox",label: "Selected", field: "selected",  autoSave : true}, "checkbox") },
            attributes : { id : {label: "Attribute number"}, name: {label: "Attribute name"} },
            statistics : { id : {label: "Statistic"}, value: {label: "Value"} },
            domains : { name : {label: "Name"}, domain : {label: "Domain"}, parameters: {label: "Parameters"} }
        };
});
