define [
  "dojo/aspect", "dojo/topic","dojo/store/Memory",
  "dijit/registry",  "dijit/layout/ContentPane", "dijit/form/TextBox", 
  "dgrid/editor",
  "custom/backend", "custom/grid", "dojo/on"
], (aspect, topic, Memory, registry, ContentPane, 
TextBox, editor, backend, grid, dojo_on) ->
  # private
  internal =
    removeTab : (tab) ->
      registry.byId("runTabs").removeChild tab
      registry.byId(tab.id).destroy()
    createTab : (tab, params_store) ->
        contentPane = new ContentPane(title: tab)
        params_grid = new grid.onDemand(
          columns:
            name:
              label: "Name"

            value: editor(
              label: "Value"
              field: "value"
              autoSave: true
            , TextBox, "click")

          store: params_store
          query: parent: tab
        )
        contentPane.addChild params_grid
        registry.byId("runTabs").addChild contentPane
    toggle : (isOn) ->
      @runs_store.put(
        id : run.id 
        selected : isOn
      ) for run in @runs_store.query(selected : not isOn)
      registry.byId("runs").refresh()
  
  # public    
  module =
    setup : ->
      conf_grid = grid.onDemand(
          columns: [
            field: "id"
            label: "Run"
          ,
          editor(
            field: "selected"
            label: "Selected"
            autoSave: true, 
            'checkbox'
          )
          ], "runs")
      
      topic.subscribe "experiment loaded from backend", (input) ->
        console.log "ssssssss"
        runs = input.runsGroup
        parameters = runs.runs.reduce(
          ((x,y) -> x.concat(y.runSpecificParameters)), [])
        parameters = parameters.concat(runs.globalLearningParameters)
        
        internal.params_store = new Memory(data: parameters)
        internal.runs_store = new Memory(
          data : (id: x, selected: true for x in runs.runsNames.concat(["globalLearningParameters"]))
        )
        conf_grid.set("store", internal.runs_store);
        
        internal.removeTab(child) for child in registry.byId("runTabs").getChildren()
        internal.createTab(run.id, internal.params_store) for run in internal.runs_store.query({})
        conf_grid.refresh()
        
      topic.subscribe "collect experiment data",  (collect) ->
        runsStore = internal.runs_store
        parametersStore = internal.params_store
        runNames = (x.id for x in runsStore.query(selected: true) when x.id isnt "globalLearningParameters")
        input =
          runsGroup:
            runsNames: runNames
            runs: (
              name: x
              runSpecificParameters: parametersStore.query(parent: x)
            ) for x in runNames
            globalLearningParameters: parametersStore.query(parent: "globalLearningParameters")
        collect input
        
      dojo_on(registry.byId("run_button"), "click", @runExperiment)

    selectAll: ->
      internal.toggle true
      
    deselectAll: ->
      internal.toggle false
      
    runExperiment: ->
      backend.runExperiment()
       
  module
      
      
        
      
     
