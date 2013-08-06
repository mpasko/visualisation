/*global define window*/
/*global define console*/
/*global define attributesStore*/
/*global define eventsStore*/
/*global define statisticsStore*/

define(["dijit/registry", "CustomGrid","Columns", "dijit/layout/ContentPane", "dojo/aspect", "Splash" ],
	function( registry, CustomGrid, columns, ContentPane, aspect,splash) {
    return {
        setup: function(){
            
            var statisticsGrid = new CustomGrid({columns: columns.statistics, store: statisticsStore}, "statisticsGrid");
            var attributesGrid = new CustomGrid({columns: columns.attributes,  store : attributesStore	},"attributesGrid");
            aspect.after(attributesStore, "setData", function() { attributesGrid.refresh();});
            
            attributesGrid.on("dgrid-select", function(event){
			    var data = [{id:"Domain", value:event.rows[0].data.domain},{id:"Parameters",value: event.rows[0].data.parameters}];
			    statisticsStore.setData(data);
			    statisticsGrid.refresh();
			});
            
            attributesGrid.on("dgrid-deselect", function(event){
			    statisticsStore.setData([]);
			    statisticsGrid.refresh();
			});

			
            var domainsGrid = new CustomGrid({	columns: columns.domains, store: domainsStore}, "domainsGrid");
			aspect.after(domainsStore, "setData", function() { domainsGrid.refresh();});
                        

			aspect.before(eventsStore, "setData", function() { registry.byId("raw_data").destroyDescendants(true);console.log("Cleared events grid"); });

			aspect.after(eventsStore, "setData", function() { 
				var eventsGrid = new CustomGrid(
				{ 
					store : eventsStore, 
					columns : attributesStore.query({}).map(function(x) { return {field : x.name, label:x.name};})
				});
				registry.byId("raw_data").addChild(eventsGrid); 
				console.log("Created new event grid");
			});
			
			splash.play("splash_aq21");
        }
    };
});

