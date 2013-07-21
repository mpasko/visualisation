/*global define window*/
/*global define console*/
/*global define FileReader*/
/*global define attributesStore*/
/*global define parametersStore*/
/*global define eventsStore*/
/*global define statisticsStore*/
/*global define runsStore*/
window.require(
	[
		"dojo/ready", "dojo/dom","dijit/registry", "dojo/_base/declare", 
		"dgrid/OnDemandGrid", "dgrid/CellSelection", "dgrid/Keyboard", "dgrid/extensions/ColumnResizer", 
		"dgrid/Selection","dgrid/extensions/DijitRegistry","dgrid/editor","dgrid/tree",
		"dijit/layout/ContentPane", "dojo/aspect", "customPanes/dataPane", "customPanes/configurationPane"
	], 
	function( ready, dom, registry, declare, 
			OnDemandGrid, CellSelection, Keyboard, ColumnResizer, Selection, DijitRegistry, editor, tree,
			ContentPane, aspect,dataPane,configurationPane) {
     ready(function(){
		// processing done when dataPane is fully loaded
		registry.byId("dataPane").set("onDownloadEnd", dataPane.setup);
		// processing done when configurationPane is fully loaded
		registry.byId("configurationPane").set("onDownloadEnd", configurationPane.setup);
		
		dom.byId("load_files").addEventListener('change', 
		function  (evt) {
			var f   = evt.target.files[0];
			var reader = new FileReader();
			reader.onload = function(e) { 
				console.log("Updating data model...");
				var input = JSON.parse(e.target.result);
				attributesStore.setData(input.attributes);
				eventsStore.setData(input.events);
				
				var parameters = [];
				input.runsGroup.runs.forEach(function (run) { parameters = parameters.concat(run.runSpecificParameters);});
				parameters = parameters.concat(input.runsGroup.globalLearningParameters);
				parametersStore.setData(parameters);
				runsStore.setData(input.runsGroup.runsNames.concat(["Global"]).map(function (x) { return { id: x, selected : true };}));
				console.log("Finished updating data model...");
			};
			
			reader.readAsText(f);
		}, false);

	});
});



