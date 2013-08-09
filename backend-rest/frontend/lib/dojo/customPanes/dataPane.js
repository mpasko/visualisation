/*global define window*/
/*global define console*/
/*global define attributesStore*/
/*global define eventsStore*/

define(["dijit/registry", "CustomGrid","Columns", "dojo/aspect","dojo/dom-construct"],
	function( registry, CustomGrid, columns, aspect,domConstruct) {
    return {
        setup: function(){
			aspect.before(eventsStore, "setData", function() { 
                registry.byId("events").destroyDescendants(false);
                domConstruct.create("div", {id: "raw_data", style :"height : 100%;"}, "events"); 
            });

			aspect.after(eventsStore, "setData", function() { 
				var eventsGrid = new CustomGrid(
				{ 
					store : eventsStore, 
					columns : attributesStore.query({}).map(function(x) { return {field : x.name, label:x.name};})
				},"raw_data");
                eventsGrid.refresh();
			});
        }
    };
});

