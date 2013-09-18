define [
  "dojo/dom","dojo/aspect","dojo/dom-construct", "dojo/_base/window","dojo/topic","dojo/store/Memory",
  "dijit/registry",
  "custom/file","custom/grid", "custom/statistics"
], (dom, aspect, domConstruct, win, topic, Memory, registry, file, grid, stats) ->
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
  # public 
  module = 
    setup : ->
      internal.createFileLoader()
      attr_grid = new grid(
        columns: [
          field: "id"
          label: "Attribute name"
        ,
          field: "name"
          label: "Attribute value"
        ], "attributes")

      domains_grid = new grid(
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

      statistics_grid = new grid(
        columns: [
          field: "id"
          label: "Statistic"
        ,
          field: "value"
          label: "Value"
        ], "statistics")
      
      attr_grid.on "dgrid-select", (event) ->
        attribute = event.rows[0].data
        stats_store = new Memory(data: [
          id: "Domain"
          value: attribute.domain
        ].concat(stats.computeStats(attribute)))
        registry.byId("statistics").set "store", stats_store
        registry.byId("statistics").refresh()

      topic.subscribe "experiment loaded", (input)->
        internal.attr_store = new Memory(data : input.attributes)
        registry.byId("attributes").set "store", internal.attr_store
        registry.byId("attributes").refresh()
        
        internal.domains_store = new Memory(data : input.domains)
        registry.byId("domains").set "store", internal.domains_store
        registry.byId("domains").refresh()
        
      topic.subscribe "collect experiment data", ->
        console.log "attributes"
        input = 
          attributes : internal.attr_store.query({})
          domains    : internal.domains_store.query({})
        topic.publish "respond experiment data", input
        
        

 
  module