require(["dojo/ready", "custom/data_sources/file", "custom/data/data", "custom/configuration/configuration", "custom/visualisation/visualisation", "custom/backend", "custom/splash", "dojo/topic"], function(ready, file, data, conf, visual, backend, splash, topic) {
  return ready(function() {
    file.createFileLoader();
    visual.setup();
    conf.setup();
    data.setup();
    topic.subscribe("convert AQ21", backend.convert.AQ21);
    topic.subscribe("convert CSV", backend.convert.CSV);
    topic.subscribe("run export", backend.runExport);
    topic.subscribe("run experiment", backend.runExperiment);
    topic.subscribe("experiment loaded from backend", data.update);
    topic.subscribe("experiment loaded from backend", conf.update);
    topic.subscribe("collect experiment data", data.collectForExperiment);
    topic.subscribe("visualise results", visual.update);
    splash.play();
  });
});
