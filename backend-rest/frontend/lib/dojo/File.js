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
		"dijit/layout/ContentPane", "dojo/aspect", "customPanes/dataPane"
],function( dom, registry, declare, 
			OnDemandGrid, CellSelection, Keyboard, ColumnResizer, Selection, DijitRegistry, editor, tree,
			ContentPane, aspect,dataPane){
    return {
        eventHandler: function  (evt) {
			var f   = evt.target.files[0];
			var reader = new FileReader();
			reader.onload = function(e) { 
				console.log("Updating data model...");
				var input = JSON.parse(e.target.result);
				attributesStore.setData(input.attributes);
				eventsStore.setData(input.events);
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
