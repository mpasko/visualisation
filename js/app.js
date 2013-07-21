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
		  "dijit/layout/ContentPane", "dojo/aspect"
	], 
	function( ready, dom, registry, declare, 
			OnDemandGrid, CellSelection, Keyboard, ColumnResizer, Selection, DijitRegistry, editor, tree,
			ContentPane, aspect) {
     ready(function(){
        // logic that requires that Dojo is fully initialized should go here
        // template for all grids used in the app
        // custom grid is obtained via mixins, which add required functionality to plain OnDemandGrid
        var CustomGrid = declare([OnDemandGrid,  Selection, Keyboard, ColumnResizer, DijitRegistry]);

		// processing done when dataPane is fully loaded
		registry.byId("dataPane").set("onDownloadEnd", function () { 
			// statistics grid displays metadata concerning currently selected attribute
			 // attributes grid displays list of attributes and the order they appear in the events
			
			var attributesGrid = new CustomGrid({
				columns: { id : {label: "#"}, name: {label: "Attribute name"} },
			    store : attributesStore
			},"attributesGrid");
            console.log("Finished creating attributes grid");
            
			var statisticsGrid = new CustomGrid({
				columns: { id : {label: "Info"}, value: {label: "Value"} },
				store: statisticsStore
			}, "statisticsGrid");
			console.log("Finished creating statistics grid");
			
			
			aspect.after(attributesStore, "setData", function() { registry.byId("attributesGrid").refresh();});
			console.log("Added updates after changes in attributesStore");
			// when we select attribute, we display info in a grid
			attributesGrid.on("dgrid-select", function(event){
			    var data = [{id:"Domain", value:event.rows[0].data.domain},{id:"Parameters",value: event.rows[0].data.parameters}];
			    statisticsStore.setData(data);
			    statisticsGrid.refresh();
			    console.log("Refreshed statistics grid");
			});
			
			attributesGrid.on("dgrid-deselect", function(event){
			    statisticsStore.setData([]);
			    statisticsGrid.refresh();
			    console.log("Cleared statistics grid");
			});
			// we destroy old grid with data
			aspect.before(eventsStore, "setData", function() { registry.byId("raw_data").destroyDescendants(true);console.log("cleared events grid"); });
			console.log("Added updates before changes in eventsStore");
			// we destroy old grid with data
			aspect.after(eventsStore, "setData", function() { 
				var eventsGrid = new CustomGrid(
				{ 
					store : eventsStore, 
					columns : attributesStore.query({}).map(function(x) { return {field : x.name, label:x.name};})
				});
				registry.byId("raw_data").addChild(eventsGrid); 
				console.log("Created new event grid");
			});
			console.log("Added updates after changes in eventsStore");
		});
		// processing done when configurationPane is fully loaded
		registry.byId("configurationPane").set("onDownloadEnd", function () { 
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
		});
		
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



