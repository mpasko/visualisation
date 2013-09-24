define [
  "dojo/dom-construct","dojo/topic","dojo/store/Memory",
  "dijit/registry", "dijit/form/TextBox",
  "dgrid/editor",
  "custom/grid"
] , (domConstruct, topic, Memory, registry, TextBox, editor,grid) ->
  # private
  internal = 
    events_store : new Memory()
    
        
  # public 
  module = 
    setup : ->
      topic.subscribe "experiment loaded", (input)->
        internal.events_store.setData input.events

      topic.subscribe "provide columns info", (columns) ->
       registry.byId("events").destroyDescendants false
       domConstruct.create("div", 
        id: "raw_data"
        style :"height : 100%;"
       , "events")
       
       events_grid = new grid(
         store : internal.events_store, 
         columns : editor(column, TextBox, "click") for column in columns
         , "raw_data")

       events_grid.refresh()
 
      topic.subscribe "collect experiment data", (collect) ->
        input = 
          events: internal.events_store.query({})
        collect input
        
      topic.subscribe "request histogram", (params) ->
        if params.isDiscrete then params.callback params.bins.map (name) -> (
          query = {}
          query[params.attr] = name
          internal.events_store.query(query).total 
        )
        
      
        
        
  module
          
