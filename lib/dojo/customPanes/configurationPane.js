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
        setup: function(){
            var CustomGrid = declare([OnDemandGrid,  Selection, Keyboard, ColumnResizer, DijitRegistry]);
            
            var runsGrid = new CustomGrid({
				columns: { id : {label: "Run"}, value: editor({name: "CheckBox",label: "Selected", field: "selected",  autoSave : true}, "checkbox") },
				store: runsStore, autosave: true
	            }, "runsGrid");
			console.log("Created runs grid");
			aspect.after(runsStore, "setData", function() { registry.byId("runsGrid").refresh();});
			
			// we destroy old tabs and grids with configuration data
			aspect.before(runsStore, "setData", function() { 
				registry.byId("runTabs").getChildren().forEach(function (child) {
					registry.byId("runTabs").removeChild(child); registry.byId(child.id).destroy(); 
				});
				console.log("Removed old tabs");
			});
			// we create new tabs and grids containing newly loaded data
			aspect.after(runsStore, "setData", function() { 
				runsStore.query({}).forEach(function (run) {
						var contentPane = new ContentPane({ title: run.id });
						var grid = new CustomGrid({
						columns: { name : {label: "#"}, value: {label: "Value"} },
			            store : parametersStore, query : { parent : run.id}});
			            
						contentPane.addChild(grid);
						registry.byId("runTabs").addChild(contentPane);
				});
				console.log("Created new tabs");
			});
        }
    };
});