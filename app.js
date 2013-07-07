/*global define window*/
/*global define FileReader*/
/*global define attributesStore*/
/*global define runConfigurationStore*/
/*global define dataStore*/
/*global define gridView*/
window.require(
	[
		"dojo/ready", "dojo/dom","dijit/registry", "dojo/_base/declare", 
		"dgrid/OnDemandGrid", "dgrid/CellSelection", "dgrid/Keyboard", "dgrid/extensions/ColumnResizer", 
		"dgrid/Selection","dgrid/extensions/DijitRegistry","dgrid/editor","dgrid/tree",
		 "classes/dialogs/GridView", "classes/input/File"
	], 
	function( ready, dom, registry, declare, 
			OnDemandGrid, CellSelection, Keyboard, ColumnResizer, Selection, DijitRegistry, editor, tree,
			GridView, FileExtractor) {
     ready(function(){
        // logic that requires that Dojo is fully initialized should go here
        
        var CustomGrid = declare([OnDemandGrid,  Selection, Keyboard, ColumnResizer, DijitRegistry]);
		var attributesGrid = new CustomGrid({
			columns: { id : {label: "#"}, name: {label: "Attribute name"} },
            store : attributesStore
            },"attributesGrid");
            
		 var configurationGrid = new CustomGrid({
			columns : [tree({label: "Id", field:"id", sortable: false}), 
					editor({label: "Value", field: "value", sortable: false}, "text", "dblclick")],
			store : runConfigurationStore
            }, "runConfigurationGrid");
        
        runConfigurationStore.getChildren =  function(parent, options){
			return parent.children;
		};
		
		runConfigurationStore.mayHaveChildren = function(parent) {
			return (parent.children && parent.children.length) || (parent.count !== 0 && parent.type !== 'parameter');
		};

		attributesGrid.on("dgrid-select", function(event){
		    // Report the item from the selected row to the console.
		    dom.byId("current_name").innerHTML = event.rows[0].data.name;
		    dom.byId("current_domain").innerHTML = event.rows[0].data.domain;
		    dom.byId("current_parameters").innerHTML = event.rows[0].data.parameters;
		    
		});
		attributesGrid.on("dgrid-deselect", function(event){
		    dom.byId("current_name").innerHTML = "";
		    dom.byId("current_domain").innerHTML = "";
		    dom.byId("current_parameters").innerHTML = "";
		});

		dom.byId("load_files").addEventListener('change', 
		function  (evt) {
			var f   = evt.target.files[0];
			var reader = new FileReader();
			reader.onload = function(e) { 
				var extractor = new FileExtractor({text: e.target.result});
				var dataGrid = new CustomGrid({ store : dataStore, columns : extractor.getColumnsName()}, "dataGrid");

				extractor.getAttributes().forEach(function (x) { attributesStore.put(x);});
				attributesGrid.refresh();
				extractor.getData().forEach(function (x) {dataStore.put(x);});
				dataGrid.refresh();
				
				extractor.getConfiguration().forEach(function (x) {runConfigurationStore.put(x);});
				console.log(runConfigurationStore);
				configurationGrid.refresh();
			};
			
			reader.readAsText(f);
			
		}, false);
		
        
        
	});
});



