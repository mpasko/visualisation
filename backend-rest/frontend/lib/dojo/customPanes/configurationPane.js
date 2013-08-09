/*global define window*/
/*global define console*/
/*global define parametersStore*/
/*global define runsStore*/
/*global define attributesStore*/
/*global define eventsStore*/
/*global define domainsStore*/

define(["dijit/registry",  "CustomGrid","Columns", "dijit/layout/ContentPane", "dojo/aspect", "Splash"],
	function( registry, CustomGrid, columns, ContentPane, aspect,splash){
    return {
        setup: function() {
            // grid containing run names and info whether its selected
            var runsGrid = new CustomGrid({columns: columns.runNames, store: runsStore, autosave: true}, "runsGrid");
			aspect.after(runsStore, "setData", function() { runsGrid.refresh();});
			
			// destroy old tabs and grids with configuration data
			aspect.before(runsStore, "setData", function() { 
				registry.byId("runTabs").getChildren().forEach(function (child) {
					registry.byId("runTabs").removeChild(child); registry.byId(child.id).destroy(); 
				});
			});
            
			// create new tabs and grids containing newly loaded data
			aspect.after(runsStore, "setData", function() { 
				runsStore.query({}).forEach(function (run) {
						var contentPane = new ContentPane({ title: run.id });
						var grid = new CustomGrid({
						columns: columns.runs,
			            store : parametersStore, query : { parent : run.id}});
			            
						contentPane.addChild(grid);
						registry.byId("runTabs").addChild(contentPane);
				});
			});
            
            // after loading this frame we show animation turning of splashes
			splash.play(["splash_aq21","splash_agh"]);			
        },
        
        // function  for compiling message from data contained in stores and sending it to backend
        createMessage : function() {
            var input = { "id" : 0 };
            
            input["attributes"] = attributesStore.query({});
            input["events"] = eventsStore.query({});
            input["domains"] = domainsStore.query({});
            
            var runNames = runsStore.query({"selected" : true}).map(function (x) { return x.id});
            runNames.splice(runNames.indexOf("globalLearningParameters"),1);
            
            input["runsGroup"] = { 
                "runsNames" : runNames, 
                "runs" : runNames.map(function (x) { return { "name" : x ,"runSpecificParameters" : parametersStore.query({ "parent" : x })};}), 
                "globalLearningParameters" : parametersStore.query({ "parent" : "globalLearningParameters" })
            };
            
            return input;
        }
    };
});
