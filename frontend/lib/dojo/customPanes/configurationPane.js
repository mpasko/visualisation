/*global define window*/
/*global define console*/
/*global define parametersStore*/
/*global define runsStore*/

define(["dijit/registry",  "CustomGrid","dgrid/editor", "dijit/layout/ContentPane", "dojo/aspect", "dojo/dom-style","dojo/_base/fx"],
	function( registry, CustomGrid, editor, ContentPane, aspect,domStyle, fx){
    return {
        setup: function() {
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
			
			domStyle.set("splash_agh", "opacity", "1");
			domStyle.set("load_files", { display: "none"  });
			var fadeArgs = { node: "splash_agh", duration : 3000 };
		    var animation = fx.fadeOut(fadeArgs);
		    animation.onEnd = function(){domStyle.set("splash_agh", {   display: "none"  });};
		    animation.play();
			
        }
    };
});