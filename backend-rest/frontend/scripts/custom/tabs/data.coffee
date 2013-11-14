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
      topic.subscribe "experiment loaded from backend", (input)->
        internal.events_store.setData input.events

      topic.subscribe "provide columns info for data tab", (columns) ->
       registry.byId("events").destroyDescendants false
       domConstruct.create("div", 
        id: "raw_data"
        style :"height : 100%;"
       , "events")
       
       events_grid = new grid.paginated(
         store : internal.events_store, 
         columns : editor(column, "text", "click") for column in columns
         pagingLinks: 3
         firstLastArrows: true
         pageSizeOptions: [20, 50, 100]
         , "raw_data")

       events_grid.refresh()
       topic.publish "provide data for scatter plot", internal.events_store.query {}
       
      # reacts for collecting data for experiment
      topic.subscribe "collect experiment data", (collect) ->
        input = events: internal.events_store.query({})
        collect input

  
  module