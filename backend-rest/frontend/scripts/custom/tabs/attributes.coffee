define [
  "dojo/dom","dojo/aspect","dojo/dom-construct", "dojo/_base/window","dojo/topic","dojo/store/Memory",
  "dijit/registry",
  "custom/file","custom/grid"
], (dom, aspect, domConstruct, win, topic, Memory, registry, file, grid) ->
  # private
  internal = 
    createFileLoader : ->
      domConstruct.create "input", 
        style: 
          display: "none" 
        type: "file"
        id: "load_files"
      , win.body()
      
      dom.byId("load_files").addEventListener 'change', file.eventHandler, false
    stats_store : new Memory()
    attr_store : new Memory()
    domains_store : new Memory()
    
    hasDefaultDomain : (attribute) ->
      attribute.domain in ["linear", "nominal", "continuous"]
    
    isDiscrete : (attribute) ->
      attribute in ["linear", "nominal"]
      
    getBaseDomain : (attribute) ->
      if @hasDefaultDomain(attribute) then attribute.domain
      else @domains_store.query(name: attribute.domain)[0].domain
      
    getBaseParameters : (attribute) ->
      if @hasDefaultDomain(attribute) then parameters = attribute.parameters
      else parameters = @domains_store.query(name: attribute.domain)[0].parameters
      parameter.trim() for parameter in /([^{}]+)/.exec(parameters)[1].split ","
    
    nominal : (parameters) ->
      console.log "nominal"
      
    linear : (parameters) ->
      console.log "linear"
      
    continuous : (parameters) ->
      @stats_store.put
        id : "Minimum"
        value : parameters[0]
      @stats_store.put
        id : "Maximum"
        value : parameters[1]
        
      registry.byId("statistics").refresh()
    
    basicStats : (attribute) ->
      @[@getBaseDomain attribute](@getBaseParameters attribute)
      
    partialRight : ( fn , args) ->
      aps = Array.prototype.slice
      args = aps.call arguments, 1
      () ->
        fn.apply this, aps.call( arguments ).concat args 
        
    cellRenderers :
      nominal: (object, value, node, options, parameters, attr_name) ->
          div = document.createElement "div"
          div.className = "renderedCell"
          div.innerHTML = object[attr_name]
          div
      
      linear: (object, value, node, options, parameters, attr_name) ->
          div = document.createElement "div"
          div.className = "renderedCell"
          div.innerHTML = object[attr_name]
          div
      
      continuous: (object, value, node, options, parameters, attr_name) ->
          div = document.createElement "div"
          div.className = "renderedCell"
          min = parseFloat parameters[0]
          max = parseFloat parameters[1]
          val = Math.round 100 * ((parseFloat object[attr_name]) - min) / (max - min)
          if val < 33 then color = "#3FFF00"
          else if val < 66 then color = "#FFD300"
          else color = "#ED1C24"
          div.style.backgroundColor = color
          div.style.width = val + "%"

          div.style.textAlign = "center"
          div.style.borderRadius = "15px"
          div.innerHTML = object[attr_name]
          div
      
      
      
  # public 
  module = 
    setup : ->
      internal.createFileLoader()
      attr_grid = new grid.onDemand(
        store : internal.attr_store
        columns: [
          field: "id"
          label: "Attribute name"
        ,
          field: "name"
          label: "Attribute value"
        ], "attributes")

      domains_grid = new grid.onDemand(
        store : internal.domains_store
        columns: [
          field: "name"
          label: "Name"
        ,
          field: "domain"
          label: "Domain"
        ,
          field: "parameters"
          label: "Parameters"
        ], "domains")

      statistics_grid = new grid.onDemand(
        store : internal.stats_store
        columns: [
          field: "id"
          label: "Statistic"
        ,
          field: "value"
          label: "Value"
        ], "statistics")
      
      attr_grid.on "dgrid-select", (event) ->
        attribute = event.rows[0].data
        internal.stats_store.setData []
        internal.basicStats attribute
        
        topic.publish "request histogram",
          attr : attribute.name
          isDiscrete : internal.isDiscrete internal.getBaseDomain attribute
          bins : internal.getBaseParameters attribute
          callback : (histogram) ->
            internal.stats_store.put
              id : "Histogram"
              value : ""
            (
              internal.stats_store.put
                id : @bins[i]
                value : histogram[i]
            ) for i in [0..histogram.length-1]
            
            registry.byId("statistics").refresh()

      topic.subscribe "experiment loaded", (input)->
        internal.attr_store.setData input.attributes
        internal.domains_store.setData input.domains
        
        
        columns = ((
          field : attribute.name
          label : attribute.name
          renderCell : internal.partialRight internal.partialRight( internal.cellRenderers[internal.getBaseDomain attribute], attribute.name), internal.getBaseParameters attribute
          )  for attribute in internal.attr_store.query {})
          
        topic.publish "provide columns info", columns
        
        registry.byId("attributes").refresh()
        registry.byId("domains").refresh()
        
      topic.subscribe "collect experiment data", (collect) ->
        input = 
          attributes : internal.attr_store.query({})
          domains    : internal.domains_store.query({})
        collect input
        
      
        
      
      
          
    
    

  module