define [
  "dojo/aspect", "dojo/store/Memory",
  "dijit/registry",  "dijit/layout/ContentPane", "dijit/form/TextBox", 
  "dgrid/editor",  "custom/backend", "custom/grid", "dojo/on"
], (aspect, Memory, registry, ContentPane, TextBox, editor, backend, grid, dojo_on) ->
  # private
  internal =
    stores:
      params : new Memory()
      runs : new Memory()
  
  # public    
  module =
    update : (input) ->
          conf_grid = registry.byId("runs")
          params_grid = registry.byId("parameters")
          
          runs = input.runsGroup
          parameters = runs.runs.reduce(
            ((x,y) -> x.concat(y.runSpecificParameters)), [])
          parameters = parameters.concat(runs.globalLearningParameters)

          internal.stores.params.setData parameters
          internal.stores.runs.setData (id: x, selected: true for x in runs.runsNames.concat(["globalLearningParameters"]))
          
          conf_grid.refresh()
          params_grid.refresh()

    createDataFromView : (collect) ->
        runsStore = internal.stores.runs
        parametersStore = internal.stores.params
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
          store: internal.stores.runs
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

        store: internal.stores.params
        query: parent: "globalLearningParameters",
        "parameters"
      )

      conf_grid.on "dgrid-select", (event) ->
        param_grid.set 'query', parent: event.rows[0].id
        param_grid.refresh()
        
      
      dojo_on(registry.byId("run_button"), "click", backend.runExperiment)
      dojo_on(registry.byId("export_button"), "click", backend.runExport)
      
      dojo_on(registry.byId("newRunButton"), "click", ->
        internal.stores.runs.put
          id: registry.byId("newRunText").value
          selected: true
          
        conf_grid = registry.byId("runs")
        conf_grid.refresh()
      )
      
      dojo_on(registry.byId("newParameterButton"), "click", ->
        params_grid = registry.byId("parameters")
        internal.stores.params.put
          name: registry.byId("newParameterText").value
          value: "value"
          parent: params_grid.query.parent
          
        params_grid.refresh()
      )
       
  module
      
      
        
      
     
