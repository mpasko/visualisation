define [
  "dojo/dom","dojo/_base/window","dojo/topic","dojo/store/Memory",
  "dijit/registry",
  "custom/grid", "dgrid/editor", "dijit/form/TextBox", "custom/cellRenderers", 
  "custom/utils/attributes"
], (dom, win, topic, Memory, registry, grid, editor, TextBox, cellRenderers, utils) ->
  # private
  internal = 
    stats_store : new Memory()
    attr_store : new Memory()
    domains_store : new Memory()
      
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
      
      attr_grid.on "dgrid-select", (event) ->
        attribute = event.rows[0].data
        domain_query = internal.domains_store.query(name: attribute.domain)
        
        desc = utils.extractAttributeDescription attribute, domain_query
        internal.stats_store.setData []
        
        internal.stats_store.put
          id : "Domain"
          value : desc.domain
        
        internal.stats_store.put
          id : "Base domain"
          value : desc.baseDomain
        
        switch desc.baseDomain
          when "linear" then internal.stats_store.put a for a in utils.getDiscreteValues desc.parameters
          when "nominal" then internal.stats_store.put a for a in utils.getDiscreteValues desc.parameters
          when "continuous" then internal.stats_store.put a for a in utils.getContinuousValues desc.parameters
        
        registry.byId("statistics").refresh()
  
      attr_grid.on "dgrid-datachange", (event) ->
        # update value, because we catch
        attribute = internal.attr_store.query({name : event.oldValue})[0]
        attribute.name = event.value
        internal.attr_store.put attribute
        
        attributes = internal.attr_store.query {}
        
        columns = ((
            field : 'attribute' + (attributes.indexOf(attribute)+1)
            label : attribute.name
            autoSave : true
            renderCell : cellRenderers.get attribute, internal.domains_store.query(name: attribute.domain)
            )  for attribute in attributes)

        topic.publish "provide columns info for data tab", columns
             
      topic.subscribe "experiment loaded from backend", (input)->
        internal.attr_store.setData input.attributes
        internal.domains_store.setData input.domains
        
        attributes = internal.attr_store.query {}
        
        columns = ((
          field : 'attribute' + (attributes.indexOf(attribute)+1)
          label : attribute.name
          autoSave : true
          renderCell : cellRenderers.get attribute, internal.domains_store.query(name: attribute.domain)
          )  for attribute in attributes)
          
        topic.subscribe "provide data for scatter plot", (data) ->
          
          # console.log data
          return 
          
          
        topic.publish "provide columns info for data tab", columns
        

        registry.byId("attributes").refresh()
        registry.byId("domains").refresh()
        
      topic.subscribe "collect experiment data", (collect) ->
        input = 
          attributes : internal.attr_store.query({})
          domains    : internal.domains_store.query({})
          
        collect input
        
  module