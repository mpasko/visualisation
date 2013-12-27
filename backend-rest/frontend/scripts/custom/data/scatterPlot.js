define(["dijit/registry", "dojo/dom-construct", "custom/grid", "dgrid/editor", "custom/data/attributes", "dijit/form/TextBox", "dojo/dom-style", "dojo/aspect", "dojo/store/Memory", "dijit/form/ComboBox"], function(registry, domConstruct, grid, editor, utils, TextBox, domStyle, aspect, Memory, ComboBox) {
  var internal, module;
  internal = {
    stores: null,
    categories: new Memory(),
    category: null,
    updatePlot: function() {
      var cell, color, data, domainsByTrait, height, mapping_discrete, mapping_numeric, n, padding, plot, size, svg, traits, width, x, xAxis, y, yAxis;
      domConstruct.empty("scatter");
      size = internal.size;
      traits = utils.getNumericAttributes(this.stores.attr, this.stores.domains);
      n = traits.length;
      internal.categories.setData(utils.getCategories(this.stores.attr, this.stores.domains));
      data = this.stores.events.query({});
      mapping_discrete = utils.getUserToData(this.stores.attr, this.stores.domains, ["integer", "linear", "nominal"]);
      mapping_numeric = utils.getDataToUser(this.stores.attr, this.stores.domains, ["integer", "continuous"]);
      domainsByTrait = utils.getDomains(this.stores.attr, this.stores.domains);
      color = d3.scale.category10();
      padding = 20;
      x = d3.scale.linear().range([padding / 2, size - padding / 2]);
      y = d3.scale.linear().range([size - padding / 2, padding / 2]);
      xAxis = d3.svg.axis().scale(x).orient("bottom").ticks(5);
      yAxis = d3.svg.axis().scale(y).orient("left").ticks(5);
      xAxis.tickSize(size * n);
      yAxis.tickSize(-size * n);
      width = size * n + 2 * padding;
      height = width;
      domStyle.set("scatter", "width", width + "px");
      domStyle.set("scatter", "height", height + "px");
      svg = d3.select("div[id=scatter]").append("svg").attr("id", "scatter_plot").attr("width", width).attr("height", height).append("g").attr("transform", "translate(" + padding + "," + padding / 2 + ")");
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
          return cell.selectAll("circle").data(data).enter().append("circle").attr("cx", function(d) {
            return x(d[p.x]);
          }).attr("cy", function(d) {
            return y(d[p.y]);
          }).attr("r", 3).style("fill", function(d) {
            return color(d[mapping_discrete[internal.category]]);
          });
        }
      };
      cell = svg.selectAll(".cell").data(utils.cross(traits, traits)).enter().append("g").attr("class", "cell").attr("transform", function(d) {
        return "translate(" + (n - d.i - 1) * size + "," + d.j * size + ")";
      }).each(plot);
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
