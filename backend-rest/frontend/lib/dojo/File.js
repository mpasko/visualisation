/*global define window*/
/*global define console*/
/*global define FileReader*/
/*global define attributesStore*/
/*global define parametersStore*/
/*global define eventsStore*/
/*global define statisticsStore*/
/*global define runsStore*/

define([
	 "dojo/dom","dijit/registry", "dojo/_base/declare", 
		"dgrid/OnDemandGrid", "dgrid/CellSelection", "dgrid/Keyboard", "dgrid/extensions/ColumnResizer", 
		"dgrid/Selection","dgrid/extensions/DijitRegistry","dgrid/editor","dgrid/tree",
		"dijit/layout/ContentPane", "dojo/aspect", "customPanes/dataPane", "dojo/request"
],function( dom, registry, declare, 
			OnDemandGrid, CellSelection, Keyboard, ColumnResizer, Selection, DijitRegistry, editor, tree,
			ContentPane, aspect,dataPane, request){
	return {
		eventHandler: function  (evt) {
			var f   = evt.target.files[0];
			var extension="";
			if(evt.target.files[0].fileName){
				extension = evt.target.files[0].fileName;
			}else{
				extension = evt.target.files[0].name;
			}
			var reader = new FileReader();
			reader.onload = function(e) { 
				console.log("Updating data model...");
				var file_content = e.target.result;
				var input="";
				if (extension.match(/\.(aq21|AQ21|a21|q21)$/))
				{
					request.post("http://localhost:9998/jersey/aq21/fromAQ21",
					{
						data: file_content,
						handleAs: "json",
						headers :
						{
							"Content-Type" : "text/plain"
						}
					}).then(function(text)
					{
						//console.log(JSON.stringify(text));
						input = text;
					});
				}
				else 
				{
					input = JSON.parse(file_content);
				}
				//console.log("before attributes");
				attributesStore.setData(input.attributes);
				//console.log("before events");
				eventsStore.setData(input.events);
				//console.log("before domains");
				domainsStore.setData(input.domains);
				window.file = input;
				var parameters = [];
				input.runsGroup.runs.forEach(function (run) { parameters = parameters.concat(run.runSpecificParameters);});
				parameters = parameters.concat(input.runsGroup.globalLearningParameters);
				parametersStore.setData(parameters);
				runsStore.setData(input.runsGroup.runsNames.concat(["globalLearningParameters"]).map(function (x) { return { id: x, selected : true };}));
				console.log("Finished updating data model...");
			};
			
			reader.readAsText(f);
		}
	};
});
