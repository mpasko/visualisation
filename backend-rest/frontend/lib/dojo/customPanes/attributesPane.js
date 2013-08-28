window.define(["dijit/registry", "CustomGrid", "dojo/aspect",  "Statistics", "util"],
    function( registry, CustomGrid,  aspect, stats, util) {
        return {
            setup: function(){
                aspect.after(attributesStore, "setData", function() { attributesGrid.refresh();});
                aspect.after(domainsStore, "setData", function() { domainsGrid.refresh();});
                aspect.after(statisticsStore, "setData", function() { statisticsGrid.refresh();});
                
                attributesGrid.on("dgrid-select", function(event){
                    var item = event.rows[0].data;
                    var data = [{id:"Domain", value:item.domain}];
                    statisticsStore.setData(data.concat(stats.computeStats(item)));
                    
                    if (stats.getRawDomain(item) == "continuous") {
                        var values = eventsStore.query({}).map(function(x) {
                            return parseFloat(x[item.name]);
                        });
                        
                        var params = stats.getRawParameters(item);
                        
                        
                        
                    }
                });

                attributesGrid.on("dgrid-deselect", function(event){
                    statisticsStore.setData([]);
                });
            }
    };
});

