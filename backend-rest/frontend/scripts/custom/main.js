require(["dojo/ready", "dijit/registry", "custom/data_sources/file", 
    "custom/data/data", "custom/configuration/configuration", 
    "custom/visualisation/visualisation", "custom/backend", 
    "custom/grid", "custom/splash", "dojo/topic"], 
    function(ready, registry, file, data, conf, visual, backend, grid, splash, topic) {
  var internal = { 
    current : null,
    setup : function() {
        var database_grid = new grid.simple({
            columns: [
              { field: "name", label: "Experiment name" }, 
              { field: "description", label: "Description" }
            ]
        }, "database_grid");

        database_grid.on("dgrid-select", function(event) {
          internal.current = event.rows[0].data;
        });

        database_grid.startup();

        registry.byId("database_button").on("click", function() {
          topic.publish("get database experiments", null);
        });

        registry.byId("load_experiment").on("click", function() {
          registry.byId("myDialog").hide();
          registry.byId("exp_description").value = internal.current.description;
          registry.byId("exp_name").value = internal.current.name;
          topic.publish("get saved experiment", internal.current.value);
        });
    }
  };
    
  ready(function() {
    file.createFileLoader();
    visual.setup();
    conf.setup();
    data.setup();
    internal.setup();
    
    topic.subscribe("convert AQ21", backend.convert.AQ21);
    topic.subscribe("convert CSV", backend.convert.CSV);
    topic.subscribe("run export", backend.runExport);
    topic.subscribe("run experiment", backend.runExperiment);
    topic.subscribe("get database experiments", backend.getExperimentList);
    topic.subscribe("get saved experiment", backend.getExperiment);
    topic.subscribe("create gld diagram", backend.getGLD);
    topic.subscribe("save experiment", backend.saveExperiment);
    topic.subscribe("experiment loaded from backend", data.update);
    topic.subscribe("experiment loaded from backend", conf.update);
    topic.subscribe("collect experiment data", data.collectForExperiment);
    topic.subscribe("visualise results", visual.update);
    topic.subscribe("algorithm selection", backend.generateConfig);
    
    topic.subscribe("render database experiments", function(experiments) {
      registry.byId("database_grid").refresh();
      registry.byId("database_grid").renderArray(experiments.experiments);
      registry.byId("myDialog").show();
    });
    
    splash.play();
  });
});
