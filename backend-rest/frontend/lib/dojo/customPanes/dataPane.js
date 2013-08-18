/*global define window*/
/*global define console*/
/*global define attributesStore*/
/*global define eventsStore*/

define(["dijit/registry", "CustomGrid", "dojo/aspect","dojo/dom-construct", "dgrid/editor","dijit/form/TextBox"],
	function( registry, CustomGrid,  aspect,domConstruct,editor,TextBox) {
    return {
        setup: function(){
            aspect.before(eventsStore, "setData", function() { 
                registry.byId("events").destroyDescendants(false);
                domConstruct.create("div", {id: "raw_data", style :"height : 100%;"}, "events"); 
            });

            aspect.after(eventsStore, "setData", function() { 
                var eventsGrid = new CustomGrid(
                { store : eventsStore, 
                  columns : attributesStore.query({}).map(function(x) { return editor({field : x.name, label:x.name, autoSave : true}, TextBox,"click");})
                },"raw_data");
                eventsGrid.refresh();
            });
        }
    };
});

