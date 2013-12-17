define ["dijit/registry","dojo/dom-construct","custom/grid", "dgrid/editor", 
"custom/data/attributes","dijit/form/TextBox","dojo/dom-style","dojo/aspect", "dojo/store/Memory","dijit/form/ComboBox"], 
(registry,domConstruct,grid,editor,utils,TextBox,domStyle, aspect, Memory, ComboBox) ->
  # private
  internal = 
    stores : null
    categories : new Memory()
    category : null
    
    updatePlot : ->
      size = internal.size
      padding = 20
      color = d3.scale.category10()
      
      data = @stores.events.query {}
      discrete = utils.getDiscreteMapping @stores, ["integer","linear", "nominal"]
      numeric = utils.getMapping @stores, ["integer", "continuous"]
      
      
      domainsByTrait = {}
      traits = []
      for domain in ([item.field, parseFloat(item.parameters[0]), parseFloat(item.parameters[1])] for item in numeric)
        domainsByTrait[domain[0]] = [domain[1], domain[2]]
        traits.push domain[0]
        
      n = numeric.length
      
      x = d3.scale.linear().range [padding / 2, size - padding / 2]
      y = d3.scale.linear().range [size - padding / 2, padding / 2]
      
      xAxis = d3.svg.axis().scale(x).orient("bottom").ticks 5
      yAxis = d3.svg.axis().scale(y).orient("left").ticks 5
      
      xAxis.tickSize( size * n )
      yAxis.tickSize( -size * n )
      
      domStyle.set "scatter", "width", (size * n + 2* padding) + "px"
      domStyle.set "scatter", "height", (size * n + 2* padding) + "px"
      domConstruct.empty "scatter"
      svg = d3.select("div[id=scatter]").append("svg").attr("id", "scatter_plot").attr("width", size * n + padding).attr("height", size * n + padding)
      .append("g").attr("transform", "translate(" + padding + "," + padding / 2 + ")")
      
      svg.selectAll(".x.axis").data(traits).enter().append("g").attr("class", "x axis")
      .attr("transform", (d, i) ->  "translate(" + (n - i - 1) * size + ",0)" )
      .each(
        (d) -> 
          x.domain(domainsByTrait[d])
          d3.select(this).call(xAxis)
      )
       
      svg.selectAll(".y.axis").data(traits).enter().append("g").attr("class", "y axis")
      .attr("transform", (d, i) -> "translate(0," + i * size + ")")
      .each((d) -> 
        y.domain(domainsByTrait[d])
        d3.select(this).call(yAxis)
      )
      
      cross = (a, b) ->
        c = []
        i = 0
        
        while i < a.length
          j = 0
          while j < b.length
            if i >= j
              c.push
                i: i
                j: j
                x: a[i]
                y: b[j]
            j++
          i++
        c
        
      discrete_values = []
      mapping_discrete = {}
      i = 0
      for item in discrete
        mapping_discrete[item.attribute.name] = item.field
        discrete_values.push { name: item.attribute.name, id : i++ }
      
      internal.categories.setData discrete_values
      
      plot = (p) ->
        cell = d3.select(this)
        x.domain(domainsByTrait[p.x])
        y.domain(domainsByTrait[p.y])

        cell.append("rect")
            .attr("class", "frame")
            .attr("x", padding / 2)
            .attr("y", padding / 2)
            .attr("width", size - padding)
            .attr("height", size - padding)
        
        if internal.category == null
          cell.selectAll("circle")
          .data(data).enter().append("circle").attr("cx", (d) -> x(d[p.x])).attr("cy", (d) -> y(d[p.y])).attr("r", 3)
        else
          console.log internal.category
          cell.selectAll("circle")
          .data(data).enter().append("circle").attr("cx", (d) -> x(d[p.x])).attr("cy", (d) -> y(d[p.y])).attr("r", 3)
          .style("fill", (d) -> color(d[mapping_discrete[internal.category]]))


      cell = svg.selectAll(".cell").data(cross(traits, traits)).enter().append("g")
      .attr("class", "cell").attr("transform", (d) -> "translate(" + (n - d.i - 1) * size + "," + d.j * size + ")").each(plot)
      
      mapping_numeric = {}
      for item in numeric
        mapping_numeric[item.field] = item.attribute.name
      
      
      
      
      
      cell.filter((d) -> d.i == d.j).append("text")
      .attr("x", padding).attr("y", padding)
      .attr("dy", ".71em").text((d) -> mapping_numeric[d.x])
     
  # public 
  module = 
    setup : (stores) ->
      internal.stores = stores
      comboBox = new ComboBox(
        id: "categories",
        store: internal.categories,
        searchAttr: "name"
      , "categories")
      
      comboBox.startup()
      comboBox.on "change", (value) ->
        internal.category = value
        internal.updatePlot()
      registry.byId("plotSize").set 'value', 150
      internal.size = 150
      registry.byId("changePlotSize").on "click", (value) ->
        internal.size = registry.byId("plotSize").value
        internal.updatePlot()
        console.log "sss"
      registry.byId("data_visualisation").watch "selectedChildWidget", 
        (name, oval, nval) ->
          if nval.title == "Scatter plot"
            internal.updatePlot()

    update : () ->
       if registry.byId("data_visualisation").selectedChildWidget.title == "Scatter plot"
          internal.updatePlot()
        
  module