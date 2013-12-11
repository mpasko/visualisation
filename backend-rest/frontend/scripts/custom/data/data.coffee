define [
  "dojo/dom","dojo/store/Memory", "dijit/registry",
  "custom/grid", "dgrid/editor", "dijit/form/TextBox", 
  "custom/data/cellRenderers", "custom/data/attributes", "dojo/dom-construct"
], (dom, Memory, registry, grid, editor, TextBox, cellRenderers, utils,domConstruct) ->
  # private
  internal =
    stores:
      attr : new Memory()
      domains : new Memory()
      events : new Memory()
    
    updateDataGrid : () ->
       attributes = @stores.attr.query {}
       selected_attributes = @stores.attr.query {selected: true}
        
       columns = (
          (
            field : 'attribute' + (attributes.indexOf(attribute)+1)
            label : attribute.name
            autoSave : true
            renderCell : cellRenderers.get attribute, internal.stores.domains.query(name: attribute.domain)
          )  for attribute in selected_attributes
       )
       
       registry.byId("events").destroyDescendants false
       domConstruct.create("div", 
        id: "datagrid"
        style :"height : 100%;"
       , "events")
       
       eventsGrid = new grid.paginated(
         store : @stores.events, 
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
        internal.stores.attr.setData input.attributes
        internal.stores.domains.setData input.domains
        internal.stores.events.setData input.events

        internal.updateDataGrid()

        registry.byId("statistics").refresh()
        registry.byId("attributes").refresh()
        registry.byId("domains").refresh()
             
    collectForExperiment : (collect) ->
        attributes = JSON.parse JSON.stringify internal.stores.attr.query({})
        
        delete a.selected for a in attributes
        
        collect 
          attributes : attributes
          domains    : internal.stores.domains.query({})
          events     : internal.stores.events.query({})
          
    setup : ->
      attr_grid = new grid.onDemand(
        store : internal.stores.attr
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
        store : internal.stores.domains
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

      statistics_grid = new grid.simple(
        store : internal.stores.stats
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
        domain_query = internal.stores.domains.query(name: attribute.domain)
        
        desc = utils.extractAttributeDescription attribute, domain_query
        array = []
        
        array.push
          id : "Domain"
          value : desc.domain
        
        array.push
          id : "Base domain"
          value : desc.baseDomain
        
        switch desc.baseDomain
          when "linear" then array.push a for a in utils.getDiscreteValues desc.parameters
          when "nominal" then array.push a for a in utils.getDiscreteValues desc.parameters
          when "continuous" then array.push a for a in utils.getContinuousValues desc.parameters
        
        registry.byId("statistics").refresh()
        registry.byId("statistics").renderArray array
  
      attr_grid.on "dgrid-datachange", (event) ->  
        el = event.cell.row.data
        el[event.cell.column.field] = event.value

        internal.stores.attr.put el

        internal.updateDataGrid()
                 
  module