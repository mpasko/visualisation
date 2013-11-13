require(["dojo/ready", "custom/tabs/visualisation", "custom/tabs/data", "custom/tabs/configuration", "custom/tabs/attributes", "custom/tabs/import", "dojo/parser", "custom/splash"], function(ready, visual, data, conf, attr, imp, parser, splash) {
  return ready(function() {
    visual.setup();
    data.setup();
    conf.setup();
    attr.setup();
    imp.setup();
    splash.play();
    return null;
  });
});
