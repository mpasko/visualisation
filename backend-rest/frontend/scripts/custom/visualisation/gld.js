define(["dojo/dom", "dijit/registry", "dojo/topic","custom/grid"], function(dom, registry, topic, grid) {
  var internal, module;
  internal = {
    results: null,
    gld : null,
    tileSize : 0,
    zip: function (arrays) {
        return arrays[0].map(function(_,i){
            return arrays.map(function(array){return array[i];});
        });
    },
    display : function (e) {
        var rect = dom.byId("myCanvas").getBoundingClientRect();
        var step = internal.tileSize + 1;
        var tilex = parseInt(e.clientX - rect.left) / step;
        var tiley = parseInt(e.clientY - rect.top) / step;
        // grid
        if ((Math.floor(tilex) === tilex)|| (Math.floor(tiley) === tiley)){
            return;
        }
        
        if (internal.gld !== null) {
            var ix = parseInt(Math.floor(tilex));
            var iy = parseInt(Math.floor(tiley));
            var values_x = internal.gld.column_sequence[ix].values.map(function(x) { return x.name;});
            var values_y = internal.gld.row_sequence[iy].values.map(function(x) { return x.name;});
            var columns = internal.gld.columns.map(function(x) { return x.name;});
            var rows = internal.gld.rows.map(function(x) { return x.name;});
            columns.reverse();
            rows.reverse();
            columns = internal.zip([columns, values_x]);
            rows = internal.zip([rows, values_y]);
            
            var sx = columns.map(function(x) { return x[0] + ": " +  x[1];}).join("</br>");
            var sy = rows.map(function(x) { return x[0] + ": " +  x[1];}).join("</br>");
            dom.byId("gld_text").innerHTML = 'Column properties: </br>' +sx + '</br>Row properties:</br>' + sy + "</br>";
        }
    },
    visualise_gld: function(gld) {
      var keys = Object.keys(gld.values).map(function(x) {return {id:x, label:x};});
      registry.byId("rules_to_select").refresh();
      registry.byId("rules_to_select").renderArray(keys);
      
      
      internal.gld = gld;
      internal.tileSize = parseInt(registry.byId("tile_size").value);
      

    }
  };
  module = {
    setup: function() {
      var rules_grid = new grid.simple({
            columns: [
              { field: "label", label: "Class to visualise" }
            ]
      }, "rules_to_select");

registry.byId("ratio_importance").set("value" , "1");
      registry.byId("repartition_probability").set("value" , "0.5"); 
      registry.byId("swap_probability").set("value" , "0.5");
      registry.byId("iterations").set("value" , "10"); 
      registry.byId("tile_size").set("value" , "8"); 
      
      
      rules_grid.on("dgrid-select", function(event) {
            var name = event.rows[0].data.id;
            var gld = internal.gld;
            
            var step = internal.tileSize + 1;
            
            var c = dom.byId("myCanvas");
            c.height = step * gld.height + 1;
            c.width = step * gld.width + 1;

            var getWidths = function(arr) {
                var grid = [ 1 ];
                for (_i = 1;_i < arr.length;_i++) {
                    var w = 0;

                    var next = arr[_i];
                    var prev = arr[_i-1];

                    var _j;
                    for (_j = 0;_j < next.values.length;_j++) {
                        if (next.values[_j].name !== prev.values[_j].name) {
                            w++;
                        }
                    }
                    grid.push(w);

                }

                grid.push(1);
                return grid;
            };
           
            
            
            
            var ctx = c.getContext("2d");
            // drawing black grid
            var _i;
            // drawing items
            ctx.fillStyle = "#00FF00";
            var _ref = gld.values[name].exists_in;
            var _len,i,j, item;
            for (_i = 0, _len = _ref.length; _i < _len; _i++) {
              item = _ref[_i];
              // getting coords from index
              var i = item % gld.width;
              var j = Math.floor(item / gld.width);
              // filling one tile
              ctx.fillRect(step * i + 1, step * j + 1, internal.tileSize, internal.tileSize);
            }
            
            var columnWidths = getWidths(gld.column_sequence);
            var _j = 0;
            ctx.fillStyle = "#000000";
            for(_i=0;_i < step *gld.width +1;_i+= step) {
                ctx.fillRect(_i, 0, columnWidths[_j], step * gld.height + 1);
                
                _j++;
            }
            
            var rowWidths = getWidths(gld.row_sequence);
            _j = 0;
            for(_i=0;_i < step *gld.height +1;_i+= step) {
                ctx.fillRect(0,_i, step * gld.width + 1,  rowWidths[_j]);
                
                
                _j++;
            }

            
      });
      
      
      dom.byId("myCanvas").addEventListener('mousemove', internal.display, false);
      topic.subscribe("visualise gld", internal.visualise_gld);
      registry.byId("load_gld").on("click", function() {
        var diagram_input =   {
          data: internal.results,
          ratio_importance: parseFloat(registry.byId("ratio_importance").value),
          repartition_probability: parseFloat(registry.byId("repartition_probability").value),
          swap_probability: parseFloat(registry.byId("swap_probability").value),
          iterations: parseInt(registry.byId("iterations").value),
          swap_enabled: true
        };
        topic.publish("create gld diagram", diagram_input);
      });
    },
    update: function(results) {
      internal.results = results;
      internal.gld = null;
      internal.tileSize = 0;
      var c = dom.byId("myCanvas");
      var ctx = c.getContext("2d");
      ctx.fillStyle = "#FFFFFF";
      ctx.fillRect(0, 0, c.height, c.width);
      registry.byId("rules_to_select").refresh();
      registry.byId("rules_to_select").renderArray([]);
    }
  };
  return module;
});
