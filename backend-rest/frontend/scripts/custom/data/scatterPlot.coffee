define ["dijit/registry","dojo/dom-construct","custom/grid", "dgrid/editor", "custom/data/attributes","dijit/form/TextBox","dojo/dom-style","dojo/aspect"], 
(registry,domConstruct,grid,editor,utils,TextBox,domStyle, aspect) ->
  # private
  internal = 
    stores : null
    width : null
    padding : 20
    height : null
    updatePlot : ->
      console.log "update plot"
      @width = domStyle.get "scatter_plot", "width"
      @height = domStyle.get "scatter_plot", "height"
      @size = 150
      
      x = d3.scale.linear().range [@padding / 2, @size - @padding / 2]
      y = d3.scale.linear().range [@size - @padding / 2, @padding / 2]
      
      xAxis = d3.svg.axis().scale(x).orient("bottom").ticks 5
      yAxis = d3.svg.axis().scale(y).orient("left").ticks 5
      
      color = d3.scale.category10()
      
      discrete = utils.getMapping @stores, ["linear", "nominal"]
      numeric = utils.getMapping @stores, ["integer", "continuous"]
      
      
      
      
      
     
  # public 
  module = 
    setup : (stores) ->
      internal.stores = stores
      domConstruct.create("svg", 
        id: "scatter_plot"
        style :"height : 100%;width:100%;"
      , "scatter")
      registry.byId("data_visualisation").watch "selectedChildWidget", 
        (name, oval, nval) ->
          if nval.title == "Scatter plot"
            internal.updatePlot()

    update : () ->
       if registry.byId("data_visualisation").selectedChildWidget.title == "Scatter plot"
          internal.updatePlot()
        
  module