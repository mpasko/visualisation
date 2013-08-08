/*global define window*/
/*global define console*/
/*global define attributesStore*/
/*global define eventsStore*/
/*global define statisticsStore*/

define(["dijit/registry", "CustomGrid","Columns", "dijit/layout/ContentPane", "dojo/aspect", "Splash" ,"dojo/dom","dojo/dom-construct"],
	function( registry, CustomGrid, columns, ContentPane, aspect,splash,dom,domConstruct) {
    return {
        setup: function(){
			aspect.before(eventsStore, "setData", function() { 
                registry.byId("events").destroyDescendants(false);
                domConstruct.create("div", {id: "raw_data", style :"height : 100%;"}, "events");
                console.log("Cleared events grid"); 
            });

			aspect.after(eventsStore, "setData", function() { 
				var eventsGrid = new CustomGrid(
				{ 
					store : eventsStore, 
					columns : attributesStore.query({}).map(function(x) { return {field : x.name, label:x.name};})
				},"raw_data");
                eventsGrid.refresh();
				console.log("Created new event grid");
			});
			
			splash.play("splash_aq21");
        }
    };
});

