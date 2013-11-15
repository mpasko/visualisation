define [
  "dojo/dom","dojo/store/Memory", "dijit/registry",
  "custom/grid", "dgrid/editor", "dijit/form/TextBox", 
  "custom/data/cellRenderers", "custom/data/attributes", "dojo/dom-construct"
], (dom, Memory, registry, grid, editor, TextBox, cellRenderers, utils,domConstruct) ->
  # private
  internal = 
    stats_store : new Memory()
    attr_store : new Memory()
    domains_store : new Memory()
    events_store : new Memory()
    
    updateDataGrid : (recreate) ->
       attributes = @attr_store.query {}
       selected_attributes = @attr_store.query {selected: true}
        
       columns = (
          (
            field : 'attribute' + (attributes.indexOf(attribute)+1)
            label : attribute.name
            autoSave : true
            renderCell : cellRenderers.get attribute, internal.domains_store.query(name: attribute.domain)
          )  for attribute in selected_attributes
       )
       
       registry.byId("events").destroyDescendants false
       domConstruct.create("div", 
        id: "datagrid"
        style :"height : 100%;"
       , "events")
       
       eventsGrid = new grid.paginated(
         store : @events_store, 
         columns : editor(column, TextBox, "click") for column in columns
         pagingLinks: 3
         firstLastArrows: true
         rowsPerPage : 20
         pageSizeOptions: [20, 50, 100]
         , "datagrid")

       eventsGrid.startup()
      
  # public 
  module = 
    updateStores: (input)->
        attribute["selected"] = true for attribute in input.attributes
        console.log input.attributes
        internal.attr_store.setData input.attributes
        internal.domains_store.setData input.domains
        internal.events_store.setData input.events
        internal.stats_store.setData []

        internal.updateDataGrid true

        registry.byId("statistics").refresh()
        registry.byId("attributes").refresh()
        registry.byId("domains").refresh()
             
    collectForExperiment : (collect) ->
        attributes = JSON.parse JSON.stringify internal.attr_store.query({})
        
        delete a.selected for a in attributes
        
        collect 
          attributes : attributes
          domains    : internal.domains_store.query({})
          events     : internal.events_store.query({})
          
    setup : ->
      attr_grid = new grid.onDemand(
        store : internal.attr_store
        columns: [
          editor(
            field: "name"
            label: "Attribute value"
            autoSave: true
          , TextBox, "dblclick")
         ,
          editor(
              field: "selected"
              label: "Selected"
              autoSave: true, 
              'checkbox'
           )
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
        ], 
        "statistics")
      
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
        el = event.cell.row.data
        el[event.cell.column.field] = event.value

        internal.attr_store.put el

        internal.updateDataGrid false
                 
  module