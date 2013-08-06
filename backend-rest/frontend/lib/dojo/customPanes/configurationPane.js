/*global define window*/
/*global define console*/
/*global define parametersStore*/
/*global define runsStore*/

define(["dijit/registry",  "CustomGrid","Columns", "dijit/layout/ContentPane", "dojo/aspect", "Splash"],
	function( registry, CustomGrid, columns, ContentPane, aspect,splash){
    return {
        setup: function() {
            var runsGrid = new CustomGrid({
				columns: columns.runNames,
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
			splash.play("splash_agh");			
        }
    };
});
