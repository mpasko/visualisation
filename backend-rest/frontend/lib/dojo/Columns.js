/*global define window*/
window.define(
	[
		"dgrid/editor"
	],
	function(editor) {
		return {
            runNames : { id : {label: "Run"}, value: editor({name: "CheckBox",label: "Selected", field: "selected",  autoSave : true}, "checkbox") },
            attributes : { id : {label: "#"}, name: {label: "Attribute name"} },
            statistics : { id : {label: "Info"}, value: {label: "Value"} },
            domains : { name : {label: "Name"}, domain : {label: "Domain"}, parameters: {label: "Parameters"} }
        };
});
