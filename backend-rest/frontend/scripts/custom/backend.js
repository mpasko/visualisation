define(["dojo/request", "dojo/topic", "dojo/_base/lang"], function(request, topic, lang) {
  var internal, module;
  internal = {
    hostname: window.location.origin + "/jersey/aq21/",
    sendMessage: function(configuration, onEnd) {
      var message;
      message = {
        id: 0
      };
      message = lang.mixin(message, configuration);
      
      topic.publish("collect experiment data", function(data) {
        message = lang.mixin(message, data);
        onEnd(JSON.stringify(message));
      });
    },
    saveExperiment: function(configuration, onEnd) {
      var message = {
        id: 0
      };
      
      configuration.value = lang.mixin(configuration.value, message);
      message = lang.mixin(configuration, message);
      
      // sending it to attributes to give us the data
      topic.publish("collect experiment data", function(data) {
        message.value = lang.mixin(message.value, data);
        onEnd(JSON.stringify(message));
      });
    }
  };
  module = {
    runExperiment: function(configuration) {
      var callback;
      callback = function(message) {
        request.post(internal.hostname + "postIt", {
          data: message,
          handleAs: "json",
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          }
        }).then((function(output) {
          if (output["outputHypotheses"].length === 0) {
            humane.log("Couldn't run experiment, check raw output");
          } else {
            humane.log("Experiment completed");
          }
          topic.publish("visualise results", output);
        }), function(error) {
          console.log("Couldn't run experiment");
        });
      };
      
      internal.sendMessage(configuration, callback);
    },
    getExperimentList: function() {
      console.log("output");
      request.get(internal.hostname + "browse", {
        handleAs: "json"
      }).then((function(output) {
        console.log(output);
        topic.publish("render database experiments", output);
      }), function(error) {
        humane.log("some error");
      });
    },
    getExperiment: function(link) {
      return request.get(internal.hostname + "browseExperiment/" + link, {
        handleAs: "json"
      }).then((function(output) {
        topic.publish("experiment loaded from backend", output);
        humane.log("Data successfully loaded from database");
      }), function(error) {
        humane.log("some error");
      });
    },
    saveExperiment: function(template) {
      var callback;
      callback = function(message) {
         
        request.post(internal.hostname + "saveExperiment", {
          data: message,
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          }
        }).then((function(output) {
          console.log("Data successfully saved");
        }), function(error) {
          console.log("some error");
        });
      };
      return internal.saveExperiment(template, callback);
    },
    getGLD: function(gld_input) {
      console.log("sadfsafsafa");
      console.log(gld_input);
      request.post(internal.hostname + "gld", {
        handleAs: "json",
        data: JSON.stringify(gld_input),
        headers: {
          "Content-Type": "application/json; charset=UTF-8"
        }
      }).then((function(output) {
        topic.publish("visualise gld", output);
      }), function(error) {
        console.log("some error");
      });
    },
    runExport: function(configuration) {
      var callback;
      callback = function(message) {
        console.log(message);
        return request.post(internal.hostname + "toAQ21", {
          data: message,
          handleAs: "text",
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          }
        }).then((function(output) {
          humane.log("Export completed");
          var textFileAsBlob = new Blob([output], {
            type: 'text/plain'
          });
          var downloadLink = document.createElement("a");
          downloadLink.download = "sample.aq21";
          downloadLink.innerHTML = "Download File";
          downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
          downloadLink.onclick = function(event) {
            document.body.removeChild(event.target);
          };
          downloadLink.style.display = "none";
          document.body.appendChild(downloadLink);
          downloadLink.click();
          console.log(output);
        }), function(error) {
          console.log("Couldn't run experiment");
        });
      };
      return internal.sendMessage(configuration, callback);
    },
    convert: {
      AQ21: function(file_content) {
        request.post(internal.hostname + "fromAQ21", {
          data: file_content,
          handleAs: "json",
          headers: {
            "Content-Type": "text/plain"
          }
        }).then((function(input) {
          topic.publish("experiment loaded from backend", input);
          humane.log("Data successfully loaded");
        }), function(error) {
          console.log("Couldn't convert AQ21 to JSON form");
        });
      },
      CSV: function(file_content) {
        return request.post(internal.hostname + "fromCSV", {
          data: file_content,
          handleAs: "json",
          headers: {
            "Content-Type": "text/plain"
          }
        }).then((function(input) {
          console.log(input);
          topic.publish("experiment loaded from backend", input);
          humane.log("Data successfully loaded");
        }), function(error) {
          console.log("Couldn't convert AQ21 to JSON form");
        });
      }
    }
  };
  return module;
});
