/* global define window */
/* global define console */
/* global define FileReader */
/* global define attributesStore */
/* global define parametersStore */
/* global define eventsStore */
/* global define statisticsStore */
/* global define runsStore */

define(["Backend"], function(backend) {
			return {
				eventHandler : function(evt) {
					var f = evt.target.files[0];
					var extension = "";
					if (evt.target.files[0].fileName) {
						extension = evt.target.files[0].fileName;
					} else {
						extension = evt.target.files[0].name;
					}
					var reader = new FileReader();
					reader.onload = function(e) {
						console.log("Updating data model...");
						var converting = function(processing, file_content) {
							if (extension.match(/\.(aq21|AQ21|a21|q21)$/)) {
								backend.convertAQ21(file_content, processing);
							} else {
								processing(JSON.parse(file_content));
							}
						}
						var processing = function(input) {
							// console.log("before attributes");
							attributesStore.setData(input.attributes);
							// console.log("before events");
							eventsStore.setData(input.events);
							// console.log("before domains");
							domainsStore.setData(input.domains);
							window.file = input;
							var parameters = [];
							input.runsGroup.runs.forEach(function(run) {
										parameters = parameters
												.concat(run.runSpecificParameters);
									});
							parameters = parameters
									.concat(input.runsGroup.globalLearningParameters);
							parametersStore.setData(parameters);
							runsStore.setData(input.runsGroup.runsNames
									.concat(["globalLearningParameters"]).map(
											function(x) {
												return {
													id : x,
													selected : true
												};
											}));
							toastr.success('Data successfully loaded');
						}

						converting(processing, e.target.result);
					};

					reader.readAsText(f);
				}
			};
		});
