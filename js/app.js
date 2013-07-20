/*global define window*/
/*global define FileReader*/
/*global define attributesStore*/
/*global define parametersStore*/
/*global define eventsStore*/
/*global define statisticsStore*/
window.require(
	[
		"dojo/ready", "dojo/dom","dijit/registry", "dojo/_base/declare", 
		"dgrid/OnDemandGrid", "dgrid/CellSelection", "dgrid/Keyboard", "dgrid/extensions/ColumnResizer", 
		"dgrid/Selection","dgrid/extensions/DijitRegistry","dgrid/editor","dgrid/tree",
		  "dijit/layout/ContentPane"
	], 
	function( ready, dom, registry, declare, 
			OnDemandGrid, CellSelection, Keyboard, ColumnResizer, Selection, DijitRegistry, editor, tree,
			ContentPane) {
     ready(function(){
        // logic that requires that Dojo is fully initialized should go here
        
        var CustomGrid = declare([OnDemandGrid,  Selection, Keyboard, ColumnResizer, DijitRegistry]);
		var attributesGrid = new CustomGrid({
			columns: { id : {label: "#"}, name: {label: "Attribute name"} },
            store : attributesStore
            },"attributesGrid");
        
        // needs to have its store
        var statisticsGrid = new CustomGrid({
			columns: { id : {label: "Info"}, value: {label: "Value"} },store: statisticsStore
            }, "statisticsGrid");

		attributesGrid.on("dgrid-select", function(event){
		    // Report the item from the selected row to the console.
		    var data = [{id:"Domain", value:event.rows[0].data.domain},{id:"Parameters",value: event.rows[0].data.parameters}];
		    statisticsStore.setData(data);
		    statisticsGrid.refresh();
		});
		attributesGrid.on("dgrid-deselect", function(event){
		    statisticsStore.setData([]);
		    statisticsGrid.refresh();
		});


		dom.byId("load_files").addEventListener('change', 
		function  (evt) {
			var f   = evt.target.files[0];
			var reader = new FileReader();
			reader.onload = function(e) { 
				registry.byId("raw_data").destroyDescendants(true);
				registry.byId("runTabs").getChildren().forEach(function (child) {registry.byId("runTabs").removeChild(child); registry.byId(child.id).destroy(); });
				
				var input = JSON.parse(e.target.result);
				attributesStore.setData(input.attributes);
				eventsStore.setData(input.events);
				
				var parameters = [];
				input.runsGroup.runs.forEach(function (run) { parameters = parameters.concat(run.runSpecificParameters);});
				parameters = parameters.concat(input.runsGroup.globalLearningParameters);
				parametersStore.setData(parameters);
				
				var eventsGrid = new CustomGrid(
					{ 
						store : eventsStore, 
						columns : input.attributes.map(function(x) { return {field : x.name, label:x.name};})
					});
				registry.byId("raw_data").addChild(eventsGrid);
				
				attributesGrid.refresh();
				
				
				
				input.runsGroup.runsNames.concat(["global"]).forEach(function (name) {
					var contentPane = new ContentPane({ title: name });
					var grid = new CustomGrid({
					columns: { name : {label: "#"}, value: {label: "Value"} },
		            store : parametersStore, query : { parent : name}});
		            
					contentPane.addChild(grid);
					registry.byId("runTabs").addChild(contentPane);
					grid.refresh();
					});
			};
			
			reader.readAsText(f);
			
		}, false);
		
        
	});
});



