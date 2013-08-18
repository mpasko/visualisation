/* global define window */
/* global define console */

define(["dojo/request"], function(request) {
			return {
				hostname : "http://localhost:9998/jersey/aq21/",
				runExperiment : function(message, processing) {
					request.post(this.hostname + "postIt", {
								data : JSON.stringify(message),
								handleAs : "json",
								headers : {
									"Content-Type" : "application/json; charset=UTF-8"
								}
							}).then(function(output) {
								processing(output);
							}, function(error) {
								toastr.error("Couldn't run experiment");
							});
				},
				convertAQ21 : function(file_content, processing) {
					request.post(this.hostname + "fromAQ21", {
								data : file_content,
								handleAs : "json",
								headers : {
									"Content-Type" : "text/plain"
								}
							}).then(function(text) {
								processing(text);
							}, function(error) {
								toastr
										.error("Couldn't convert AQ21 to JSON form");
							});
				}
			};
		});
