/*global define window*/
/*global define console*/
/*global define attributesStore*/
/*global define statisticsStore*/

define(["dijit/registry", "CustomGrid","Columns", "dijit/layout/ContentPane", "dojo/aspect",  "Statistics"],
	function( registry, CustomGrid, columns, ContentPane, aspect, stats) {
    return {
        setup: function(){
            // grid for displaying statistical data about attributes
            var statisticsGrid = new CustomGrid({columns: columns.statistics, store: statisticsStore}, "statisticsGrid");
            // grid for displaying attributes with their numbers
            var attributesGrid = new CustomGrid({columns: columns.attributes,  store : attributesStore	},"attributesGrid");
            aspect.after(attributesStore, "setData", function() { attributesGrid.refresh();});
            
            attributesGrid.on("dgrid-select", function(event){
                var item = event.rows[0].data;
                var data = [{id:"Domain", value:item.domain}];
                

			    statisticsStore.setData(data.concat(stats.computeStats(item)));
			    statisticsGrid.refresh();
			});
            
            attributesGrid.on("dgrid-deselect", function(event){
			    statisticsStore.setData([]);
			    statisticsGrid.refresh();
			});

			// grid for displaying domains of attributes
            var domainsGrid = new CustomGrid({	columns: columns.domains, store: domainsStore}, "domainsGrid");
			aspect.after(domainsStore, "setData", function() { domainsGrid.refresh();});

        }
    };
});

