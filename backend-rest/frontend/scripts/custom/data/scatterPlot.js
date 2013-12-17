(function() {
  define(["dijit/registry", "dojo/dom-construct", "custom/grid", "dgrid/editor", "custom/data/attributes", "dijit/form/TextBox", "dojo/dom-style", "dojo/aspect", "dojo/store/Memory", "dijit/form/ComboBox"], function(registry, domConstruct, grid, editor, utils, TextBox, domStyle, aspect, Memory, ComboBox) {
    var internal, module;
    internal = {
      stores: null,
      categories: new Memory(),
      category: null,
      updatePlot: function() {
        var cell, color, cross, data, discrete, discrete_values, domain, domainsByTrait, i, item, mapping_discrete, mapping_numeric, n, numeric, padding, plot, size, svg, traits, x, xAxis, y, yAxis, _i, _j, _k, _len, _len1, _len2, _ref;
        size = internal.size;
        padding = 20;
        color = d3.scale.category10();
        data = this.stores.events.query({});
        discrete = utils.getDiscreteMapping(this.stores, ["integer", "linear", "nominal"]);
        numeric = utils.getMapping(this.stores, ["integer", "continuous"]);
        domainsByTrait = {};
        traits = [];
        _ref = (function() {
          var _j, _len, _results;
          _results = [];
          for (_j = 0, _len = numeric.length; _j < _len; _j++) {
            item = numeric[_j];
            _results.push([item.field, parseFloat(item.parameters[0]), parseFloat(item.parameters[1])]);
          }
          return _results;
        })();
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          domain = _ref[_i];
          domainsByTrait[domain[0]] = [domain[1], domain[2]];
          traits.push(domain[0]);
        }
        n = numeric.length;
        x = d3.scale.linear().range([padding / 2, size - padding / 2]);
        y = d3.scale.linear().range([size - padding / 2, padding / 2]);
        xAxis = d3.svg.axis().scale(x).orient("bottom").ticks(5);
        yAxis = d3.svg.axis().scale(y).orient("left").ticks(5);
        xAxis.tickSize(size * n);
        yAxis.tickSize(-size * n);
        domStyle.set("scatter", "width", (size * n + 2 * padding) + "px");
        domStyle.set("scatter", "height", (size * n + 2 * padding) + "px");
        domConstruct.empty("scatter");
        svg = d3.select("div[id=scatter]").append("svg").attr("id", "scatter_plot").attr("width", size * n + padding).attr("height", size * n + padding).append("g").attr("transform", "translate(" + padding + "," + padding / 2 + ")");
        svg.selectAll(".x.axis").data(traits).enter().append("g").attr("class", "x axis").attr("transform", function(d, i) {
          return "translate(" + (n - i - 1) * size + ",0)";
        }).each(function(d) {
          x.domain(domainsByTrait[d]);
          return d3.select(this).call(xAxis);
        });
        svg.selectAll(".y.axis").data(traits).enter().append("g").attr("class", "y axis").attr("transform", function(d, i) {
          return "translate(0," + i * size + ")";
        }).each(function(d) {
          y.domain(domainsByTrait[d]);
          return d3.select(this).call(yAxis);
        });
        cross = function(a, b) {
          var c, i, j;
          c = [];
          i = 0;
          while (i < a.length) {
            j = 0;
            while (j < b.length) {
              if (i >= j) {
                c.push({
                  i: i,
                  j: j,
                  x: a[i],
                  y: b[j]
                });
              }
              j++;
            }
            i++;
          }
          return c;
        };
        discrete_values = [];
        mapping_discrete = {};
        i = 0;
        for (_j = 0, _len1 = discrete.length; _j < _len1; _j++) {
          item = discrete[_j];
          mapping_discrete[item.attribute.name] = item.field;
          discrete_values.push({
            name: item.attribute.name,
            id: i++
          });
        }
        internal.categories.setData(discrete_values);
        plot = function(p) {
          var cell;
          cell = d3.select(this);
          x.domain(domainsByTrait[p.x]);
          y.domain(domainsByTrait[p.y]);
          cell.append("rect").attr("class", "frame").attr("x", padding / 2).attr("y", padding / 2).attr("width", size - padding).attr("height", size - padding);
          if (internal.category === null) {
            return cell.selectAll("circle").data(data).enter().append("circle").attr("cx", function(d) {
              return x(d[p.x]);
            }).attr("cy", function(d) {
              return y(d[p.y]);
            }).attr("r", 3);
          } else {
            console.log(internal.category);
            return cell.selectAll("circle").data(data).enter().append("circle").attr("cx", function(d) {
              return x(d[p.x]);
            }).attr("cy", function(d) {
              return y(d[p.y]);
            }).attr("r", 3).style("fill", function(d) {
              return color(d[mapping_discrete[internal.category]]);
            });
          }
        };
        cell = svg.selectAll(".cell").data(cross(traits, traits)).enter().append("g").attr("class", "cell").attr("transform", function(d) {
          return "translate(" + (n - d.i - 1) * size + "," + d.j * size + ")";
        }).each(plot);
        mapping_numeric = {};
        for (_k = 0, _len2 = numeric.length; _k < _len2; _k++) {
          item = numeric[_k];
          mapping_numeric[item.field] = item.attribute.name;
        }
        return cell.filter(function(d) {
          return d.i === d.j;
        }).append("text").attr("x", padding).attr("y", padding).attr("dy", ".71em").text(function(d) {
          return mapping_numeric[d.x];
        });
      }
    };
    module = {
      setup: function(stores) {
        var comboBox;
        internal.stores = stores;
        comboBox = new ComboBox({
          id: "categories",
          store: internal.categories,
          searchAttr: "name"
        }, "categories");
        comboBox.startup();
        comboBox.on("change", function(value) {
          internal.category = value;
          return internal.updatePlot();
        });
        registry.byId("plotSize").set('value', 150);
        internal.size = 150;
        registry.byId("changePlotSize").on("click", function(value) {
          internal.size = registry.byId("plotSize").value;
          internal.updatePlot();
          return console.log("sss");
        });
        return registry.byId("data_visualisation").watch("selectedChildWidget", function(name, oval, nval) {
          if (nval.title === "Scatter plot") {
            return internal.updatePlot();
          }
        });
      },
      update: function() {
        if (registry.byId("data_visualisation").selectedChildWidget.title === "Scatter plot") {
          return internal.updatePlot();
        }
      }
    };
    return module;
  });

}).call(this);
