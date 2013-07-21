/*global define window*/
/*global define console*/
/*global define attributesStore*/
/*global define eventsStore*/
/*global define statisticsStore*/

define(["dijit/registry", "CustomGrid","dgrid/editor", "dijit/layout/ContentPane", "dojo/aspect", "dojo/dom-style","dojo/_base/fx" ],
	function( registry, CustomGrid, editor, ContentPane, aspect,domStyle, fx) {
    return {
        setup: function(){
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
			
			
			domStyle.set("splash", "opacity", "1");
			domStyle.set("load_files", { display: "none"  });
			var fadeArgs = { node: "splash", duration : 1000 };
		    var animation = fx.fadeOut(fadeArgs);
		    animation.onEnd = function(){domStyle.set("splash", {   display: "none"  });};
		    animation.play();
        }
    };
});

