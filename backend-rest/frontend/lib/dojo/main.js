/* global define window */
/* global define console */
window.require(["dojo/dom", "dojo/ready", "dijit/registry",
				"customPanes/attributesPane", "customPanes/dataPane",
				"customPanes/configurationPane",
				"customPanes/visualisationPane", "File", "dojo/dom-style"],
		function(dom, ready, registry, attributesPane, dataPane,
				configurationPane, visualisationPane, file, domStyle) {
			ready(function() {
						// each content pane has its js counterpart with setup
						// function, which performs custom configuration of the
						// pane content
						// after itś downloaded from the url in href
						registry.byId("attributesPane").set("onDownloadEnd",
								attributesPane.setup);
						registry.byId("dataPane").set("onDownloadEnd",
								dataPane.setup);
						registry.byId("configurationPane").set("onDownloadEnd",
								configurationPane.setup);
						registry.byId("visualisationPane").set("onDownloadEnd",
								visualisationPane.setup);
						
						// hooking custom event handler for loading files
						// and hiding default original button
						dom.byId("load_files").addEventListener('change',
								file.eventHandler, false);
						domStyle.set("load_files", {
									display : "none"
								});
					});
		});
