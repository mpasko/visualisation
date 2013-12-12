define [
  "dojo/request","dojo/topic","dojo/_base/lang"
], (request, topic, lang) ->
  # private
  internal = 
    hostname: window.location.origin + "/jersey/aq21/"
    sendMessage : (configuration, onEnd)->
      message = 
        id:0
      message = lang.mixin message, configuration
      
      topic.publish "collect experiment data", 
        (data) ->
          message = lang.mixin message, data
          onEnd(JSON.stringify message)
      
  # public 
  module = 
    runExperiment: (configuration) ->
      callback = (message) ->
        console.log message
        request.post(internal.hostname + "postIt",
          data:  message
          handleAs: "json"
          headers:
            "Content-Type": "application/json; charset=UTF-8"
        ).then ((output) ->
          humane.log "Experiment completed"
          topic.publish "visualise results", output
        ), (error) ->
          console.log "Couldn't run experiment"
          
      internal.sendMessage(configuration,callback) 
    
    runExport: (configuration) ->
      callback = (message) ->
        console.log message
        request.post(internal.hostname + "toAQ21",
          data:  message
          handleAs: "text"
          headers:
            "Content-Type": "application/json; charset=UTF-8"
        ).then ((output) ->
          humane.log "Export completed"
          console.log output
        ), (error) ->
          console.log "Couldn't run experiment"
          
      internal.sendMessage(configuration,callback) 
      
    convert :  
      AQ21: (file_content) ->  
        topic.publish "experiment raw text loaded", file_content
        request.post(internal.hostname + "fromAQ21",
          data: file_content
          handleAs: "json"
          headers:
            "Content-Type": "text/plain"
        ).then ((input) ->
          topic.publish "experiment loaded from backend", input
          humane.log "Data successfully loaded"
        ), (error) ->
          console.log "Couldn't convert AQ21 to JSON form"


      CSV: (file_content) -> 
        topic.publish "experiment raw text loaded", file_content
        request.post(internal.hostname + "fromCSV",
          data: file_content
          handleAs: "json"
          headers:
            "Content-Type": "text/plain"
        ).then ((input) ->
          console.log input
          topic.publish "experiment loaded from backend", input
          humane.log "Data successfully loaded"
        ), (error) ->
          console.log "Couldn't convert AQ21 to JSON form"
  
  module