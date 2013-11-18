define [
  "dojo/aspect", "dojo/store/Memory",
  "dijit/registry",  "dijit/layout/ContentPane", "dijit/form/TextBox", 
  "dgrid/editor",  "custom/backend", "custom/grid", "dojo/on"
], (aspect, Memory, registry, ContentPane, TextBox, editor, backend, grid, dojo_on) ->
  # private
  internal =
    params_store : new Memory()
    runs_store : new Memory()
  
  # public    
  module =
    createViewFromData : (input) ->
          conf_grid = registry.byId("runs")
          params_grid = registry.byId("parameters")
          
          runs = input.runsGroup
          parameters = runs.runs.reduce(
            ((x,y) -> x.concat(y.runSpecificParameters)), [])
          parameters = parameters.concat(runs.globalLearningParameters)

          internal.params_store.setData parameters
          internal.runs_store.setData (id: x, selected: true for x in runs.runsNames.concat(["globalLearningParameters"]))
          
          conf_grid.refresh()
          params_grid.refresh()

    createDataFromView : (collect) ->
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
        
    setup : ->
      conf_grid = new grid.onDemand(
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
          ], 
          store: internal.runs_store
          "runs")
      
      param_grid = new grid.onDemand(
        columns:
          name:
            label: "Name"

          value: editor(
            label: "Value"
            field: "value"
            autoSave: true
          , TextBox, "click")

        store: internal.params_store
        query: parent: "globalLearningParameters",
        "parameters"
      )

      conf_grid.on "dgrid-select", (event) ->
        param_grid.set 'query', parent: event.rows[0].id
        param_grid.refresh()
        
      
      dojo_on(registry.byId("run_button"), "click", backend.runExperiment)
      dojo_on(registry.byId("export_button"), "click", backend.runExport)
      
      dojo_on(registry.byId("newRunButton"), "click", ->
        internal.runs_store.put
          id: registry.byId("newRunText").value
          selected: true
          
        conf_grid = registry.byId("runs")
        conf_grid.refresh()
      )
      
      dojo_on(registry.byId("newParameterButton"), "click", ->
        params_grid = registry.byId("parameters")
        internal.params_store.put
          name: registry.byId("newParameterText").value
          value: "value"
          parent: params_grid.query.parent
          
        params_grid.refresh()
      )
       
  module
      
      
        
      
     
