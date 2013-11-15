require(["dojo/ready", "custom/import/import", "custom/data/data", "custom/configuration/configuration", "custom/visualisation/visualisation", "custom/splash", "dojo/topic"], function(ready, importer, data, conf, visual, splash, topic) {
  return ready(function() {
    visual.setup();
    conf.setup();
    data.setup();
    importer.setup();
    topic.subscribe("experiment raw text loaded", importer.textProvided);
    topic.subscribe("experiment loaded from backend", data.updateStores);
    topic.subscribe("experiment loaded from backend", conf.createViewFromData);
    topic.subscribe("collect experiment data", data.collectForExperiment);
    topic.subscribe("collect experiment data", conf.createDataFromView);
    topic.subscribe("visualise results", visual.visualiseResults);
    splash.play();
  });
});
