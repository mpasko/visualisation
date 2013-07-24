/*global define window*/
/*global define console*/
window.require(
	[
		"dojo/ready", "dojo/dom","dijit/registry",
		"customPanes/dataPane", "customPanes/configurationPane", "File"
	], 
	function(ready, dom, registry, dataPane, configurationPane, file) {
     ready(function() {
		registry.byId("dataPane").set("onDownloadEnd", dataPane.setup);
		registry.byId("configurationPane").set("onDownloadEnd", configurationPane.setup);
		dom.byId("load_files").addEventListener('change', file.eventHandler, false);
	});
});