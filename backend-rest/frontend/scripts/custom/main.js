require(["dojo/ready", "custom/tabs/visualisation", "custom/tabs/data", "custom/tabs/configuration", "custom/tabs/attributes", "dojo/parser", "custom/splash"], function(ready, visual, data, conf, attr, parser, splash) {
  return ready(function() {
    visual.setup();
    data.setup();
    conf.setup();
    attr.setup();
    parser.parse();
    splash.play();
    return null;
  });
});
