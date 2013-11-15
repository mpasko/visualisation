define(["dojo/request", "dojo/topic", "dojo/_base/lang"], function(request, topic, lang) {
  var internal, module;
  internal = {
    hostname: window.location.origin + "/jersey/aq21/",
    sendMessage: function(onEnd) {
      var counter, message;
      message = {
        id: 0
      };
      counter = 0;
      return topic.publish("collect experiment data", function(input) {
        message = lang.mixin(message, input);
        counter = counter + 1;
        if (counter === 2) {
          return onEnd(JSON.stringify(message));
        }
      });
    }
  };
  module = {
    runExperiment: function() {
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
          humane.log("Experiment completed");
          return topic.publish("visualise results", output);
        }), function(error) {
          return console.log("Couldn't run experiment");
        });
      };
      return internal.sendMessage(callback);
    },
    runExport: function() {
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
          return console.log(output);
        }), function(error) {
          return console.log("Couldn't run experiment");
        });
      };
      return internal.sendMessage(callback);
    },
    convert: {
      AQ21: function(file_content) {
        topic.publish("experiment raw text loaded", file_content);
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
        topic.publish("experiment raw text loaded", file_content);
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
