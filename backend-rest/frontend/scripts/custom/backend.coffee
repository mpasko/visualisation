define [
  "dojo/request","dojo/topic","dojo/_base/lang"
], (request, topic, lang) ->
  # private
  internal = 
    hostname: window.location.origin + "/jersey/aq21/"
    sendMessage : (onEnd)->
      message = {id:0}
      counter = 0
              
      topic.publish "collect experiment data", 
        (input) ->
          message = lang.mixin message, input
          counter = counter + 1
          onEnd(JSON.stringify message) if counter == 3
      
  # public 
  module = 
    runExperiment: () ->
      callback = (message) ->
        request.post(internal.hostname + "postIt",
          data:  message
          handleAs: "json"
          headers:
            "Content-Type": "application/json; charset=UTF-8"
        ).then ((output) ->
          humane.log "Experiment completed"
          topic.publish "visualise results", output["outputHypotheses"]
          topic.publish "raw format", output["raw_aq21"]
        ), (error) ->
          console.log "Couldn't run experiment"
          
      internal.sendMessage(callback) 

    convertAQ21: (file_content) ->
      topic.publish "experiment raw text", file_content
    
      request.post(internal.hostname + "fromAQ21",
        data: file_content
        handleAs: "json"
        headers:
          "Content-Type": "text/plain"
      ).then ((input) ->
        topic.publish "experiment loaded", input
        humane.log "Data successfully loaded"
      ), (error) ->
        console.log "Couldn't convert AQ21 to JSON form"
  
  module