define(["dijit/registry", "dojo/dom-construct", "custom/grid", "dgrid/editor", "custom/data/attributes", "dijit/form/TextBox", "dojo/dom-style", "dojo/aspect"], function(registry, domConstruct, grid, editor, utils, TextBox, domStyle, aspect) {
  var internal, module;
  internal = {
    stores: null,
    width: null,
    padding: 20,
    height: null,
    updatePlot: function() {
      var color, discrete, numeric, x, xAxis, y, yAxis;
      console.log("update plot");
      this.width = domStyle.get("scatter_plot", "width");
      this.height = domStyle.get("scatter_plot", "height");
      this.size = 150;
      x = d3.scale.linear().range([this.padding / 2, this.size - this.padding / 2]);
      y = d3.scale.linear().range([this.size - this.padding / 2, this.padding / 2]);
      xAxis = d3.svg.axis().scale(x).orient("bottom").ticks(5);
      yAxis = d3.svg.axis().scale(y).orient("left").ticks(5);
      color = d3.scale.category10();
      discrete = utils.getMapping(this.stores, ["linear", "nominal"]);
      return numeric = utils.getMapping(this.stores, ["integer", "continuous"]);
    }
  };
  module = {
    setup: function(stores) {
      internal.stores = stores;
      domConstruct.create("svg", {
        id: "scatter_plot",
        style: "height : 100%;width:100%;"
      }, "scatter");
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
