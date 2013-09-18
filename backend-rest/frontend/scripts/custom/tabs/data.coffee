define [
  "dojo/dom-construct","dojo/topic","dojo/store/Memory",
  "dijit/registry", "dijit/form/TextBox",
  "dgrid/editor",
  "custom/grid"
] , (domConstruct, topic, Memory, registry, TextBox, editor,grid) ->
  # private
  internal = {}
  # public 
  module = 
    setup : ->
      topic.subscribe "experiment loaded", (input)->
        internal.events_store = new Memory(data: input.events)
        registry.byId("events").destroyDescendants false
        domConstruct.create("div", 
          id: "raw_data"
          style :"height : 100%;"
        , "events")

        events_grid = new grid(
          store : internal.events_store, 
          columns : (editor(
              field : attribute.name
              label : attribute.name
              autoSave : true, 
              TextBox, "click")
          ) for attribute in input.attributes
          , "raw_data")
        
        events_grid.refresh()
        
      topic.subscribe "collect experiment data", ->
        console.log "data"
        input = 
          events: internal.events_store.query({})
        
        topic.publish "respond experiment data", input
  module
          
