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
        }
    };
});

