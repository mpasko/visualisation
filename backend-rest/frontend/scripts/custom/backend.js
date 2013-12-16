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
      return topic.publish("collect experiment data", function(data) {
        message = lang.mixin(message, data);
        return onEnd(JSON.stringify(message));
      });
    }
  };
  module = {
    runExperiment: function(configuration) {
      var callback;
      callback = function(message) {
        console.log(message);
        return request.post(internal.hostname + "postIt", {
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
          return topic.publish("visualise results", output);
        }), function(error) {
          return console.log("Couldn't run experiment");
        });
      };
      return internal.sendMessage(configuration, callback);
    },
    getExperimentList: function() {
      return request.get(internal.hostname + "browse", {
        handleAs: "json"
      }).then((function(output) {
        return topic.publish("render database experiments", output);
      }), function(error) {
        return humane.log("some error");
      });
    },
    getExperiment: function(link) {
      return request.get(internal.hostname + "browseExperiment/" + link, {
        handleAs: "json"
      }).then((function(output) {
        topic.publish("experiment loaded from backend", output);
        return humane.log("Data successfully loaded from database");
      }), function(error) {
        return humane.log("some error");
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
          var downloadLink, textFileAsBlob;
          humane.log("Export completed");
          textFileAsBlob = new Blob([output], {
            type: 'text/plain'
          });
          downloadLink = document.createElement("a");
          downloadLink.download = "sample.aq21";
          downloadLink.innerHTML = "Download File";
          downloadLink.href = window.URL.createObjectURL(textFileAsBlob);
          downloadLink.onclick = function(event) {
            return document.body.removeChild(event.target);
          };
          downloadLink.style.display = "none";
          document.body.appendChild(downloadLink);
          downloadLink.click();
          return console.log(output);
        }), function(error) {
          return console.log("Couldn't run experiment");
        });
      };
      return internal.sendMessage(configuration, callback);
    },
    convert: {
      AQ21: function(file_content) {
        return request.post(internal.hostname + "fromAQ21", {
          data: file_content,
          handleAs: "json",
          headers: {
            "Content-Type": "text/plain"
          }
        }).then((function(input) {
          topic.publish("experiment loaded from backend", input);
          return humane.log("Data successfully loaded");
        }), function(error) {
          return console.log("Couldn't convert AQ21 to JSON form");
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
          return humane.log("Data successfully loaded");
        }), function(error) {
          return console.log("Couldn't convert AQ21 to JSON form");
        });
      }
    }
  };
  return module;
});
