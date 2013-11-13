define [
  "dojo/dom","dojo/_base/window","dojo/topic","dojo/store/Memory",
  "dijit/registry",
  "custom/grid", "dgrid/editor", "dijit/form/TextBox", "custom/cellRenderers", "custom/utils"
], (dom, win, topic, Memory, registry, grid, editor, TextBox, cellRenderers, utils) ->
  # private
  internal = 
    stats_store : new Memory()
    attr_store : new Memory()
    domains_store : new Memory()
    
    hasDefaultDomain : (attribute) ->
      attribute.domain in ["linear", "nominal", "continuous"]
    
    isDiscrete : (attribute) ->
      attribute in ["linear", "nominal"]
    
    # whether domain specified in domain field is one of the basic domains
    # or custom, user-defined domain
    getBaseDomain : (attribute) ->
      if @hasDefaultDomain(attribute) then attribute.domain
      else @domains_store.query(name: attribute.domain)[0].domain
    
    # parameters of domain, splitted into a list
    getBaseParameters : (attribute) ->
      if @hasDefaultDomain(attribute) then parameters = attribute.parameters
      else parameters = @domains_store.query(name: attribute.domain)[0].parameters
      parameter.trim() for parameter in /([^{}]+)/.exec(parameters)[1].split ","
    
    extractAttributeDescription : (attribute) ->
      domain : attribute.domain
      baseDomain : @getBaseDomain attribute
      parameters : @getBaseParameters attribute
      
  # public 
  module = 
    setup : ->
      attr_grid = new grid.onDemand(
        store : internal.attr_store
        columns: [
          editor(
            field: "name"
            label: "Attribute value"
            autoSave: true
          , TextBox, "dblclick")
        ], 
        "attributes")

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
      
      # when item in attributes is selected
      # then we display some statistics about it
      attr_grid.on "dgrid-select", (event) ->
        attribute = event.rows[0].data
        internal.stats_store.setData []
        
        description = internal.extractAttributeDescription attribute
        
        internal.stats_store.put
          id : "Domain"
          value : description.domain
        
        internal.stats_store.put
          id : "Base domain"
          value : description.baseDomain
        
        addMinMax = (params) ->
          internal.stats_store.put
            id : "Minimum"
            value : params[0]
          
          internal.stats_store.put
            id : "Maximum"
            value : params[1]
        
        addNominalValues = (params) ->
          
          i = 0
          while i < params.length
            internal.stats_store.put
              id : i
              value : params[i]
            
            i++
        
        switch description.baseDomain
          when "linear" then addNominalValues description.parameters
          when "nominal" then addNominalValues description.parameters
          when "continuous" then addMinMax description.parameters
        
        
        registry.byId("statistics").refresh()
  
      #
      attr_grid.on "dgrid-datachange", (event) ->
        attributes = internal.attr_store.query {}
        
        i = 0
        while i < attributes.length
          attributes[i].name = event.value if attributes[i].name == event.oldValue
          i++
        
        console.log event
        columns = ((
            field : 'attribute' + (attributes.indexOf(attribute)+1)
            label : attribute.name
            autoSave : true
            renderCell : utils.partialRight utils.partialRight( cellRenderers[internal.getBaseDomain attribute], attribute.name), internal.getBaseParameters attribute
            )  for attribute in attributes)

        topic.publish "provide columns info", columns
            

      topic.subscribe "experiment loaded", (input)->
        internal.attr_store.setData input.attributes
        internal.domains_store.setData input.domains
        
        attributes = internal.attr_store.query {}
        
        columns = ((
          field : 'attribute' + (attributes.indexOf(attribute)+1)
          label : attribute.name
          autoSave : true
          renderCell : utils.partialRight utils.partialRight( cellRenderers[internal.getBaseDomain attribute], attribute.name), internal.getBaseParameters attribute
          )  for attribute in attributes)
          
        topic.publish "provide columns info", columns
        
        registry.byId("attributes").refresh()
        registry.byId("domains").refresh()
        
      topic.subscribe "collect experiment data", (collect) ->
        input = 
          attributes : internal.attr_store.query({})
          domains    : internal.domains_store.query({})
          
        collect input
        
  module