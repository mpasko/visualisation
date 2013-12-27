require [
  "dojo/ready",
  "dijit/registry",
  "custom/data_sources/file",
  "custom/data/data",
  "custom/configuration/configuration",
  "custom/visualisation/visualisation",
  "custom/backend",
  "custom/grid",
  "custom/splash",
  "dojo/topic"
], (ready, registry, file,data, conf ,visual, backend,grid, splash, topic) ->
    ready(
      ->
        file.createFileLoader()
      
        visual.setup()
        conf.setup()
        data.setup()
        
        topic.subscribe "convert AQ21", backend.convert.AQ21
        topic.subscribe "convert CSV",  backend.convert.CSV
        topic.subscribe "run export", backend.runExport
        topic.subscribe "run experiment", backend.runExperiment
        topic.subscribe "get database experiments", backend.getExperimentList
        topic.subscribe "get saved experiment", backend.getExperiment
        topic.subscribe "create gld diagram", backend.getGLD
        topic.subscribe "save experiment", backend.saveExperiment
        
        topic.subscribe "experiment loaded from backend", data.update
        topic.subscribe "experiment loaded from backend", conf.update
        
        topic.subscribe "collect experiment data",  data.collectForExperiment
        topic.subscribe "visualise results", visual.update
        topic.subscribe "render database experiments",
          (experiments) ->
            registry.byId("database_grid").refresh()
            registry.byId("database_grid").renderArray experiments.experiments
            registry.byId("myDialog").show()
            
            
        
        database_grid = new grid.simple(
              columns: [ {
                field: "name"
                label: "Experiment name"
              }, {
                field: "description"
                label: "Description"
              }], 
              "database_grid")
        
        
        current = null
        database_grid.on "dgrid-select", (event) ->
          current = event.rows[0].data
        
        database_grid.startup()
        
        registry.byId("database_button").on "click", 
          ->
            topic.publish "get database experiments", null
            
        registry.byId("load_experiment").on "click", 
          ->
            registry.byId("myDialog").hide()
            registry.byId("exp_description").value = current.description
            registry.byId("exp_name").value = current.name
            topic.publish "get saved experiment", current.value
        
        splash.play()
        return
    )
