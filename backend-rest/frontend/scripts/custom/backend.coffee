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
          if output["outputHypotheses"].length == 0
            humane.log "Couldn't run experiment, check raw output"
          else
             humane.log "Experiment completed"
          topic.publish "visualise results", output
        ), (error) ->
          console.log "Couldn't run experiment"
          
      internal.sendMessage(configuration,callback) 
      
      
    getExperimentList: () ->
      request.get(internal.hostname + "browse",
        handleAs: "json"
      ).then ((output) ->
        topic.publish "render database experiments", output
      ), (error) ->
        humane.log "some error"
        
    getExperiment: (link) ->
      request.get(internal.hostname + "browseExperiment/" + link,
        handleAs: "json"
      ).then ((output) ->
        topic.publish "experiment loaded from backend", output
        
        humane.log "Data successfully loaded from database"
      ), (error) ->
        humane.log "some error"
				
    saveExperiment: (configuration) ->
      callback = (message)
        console.log experiment
        request.post(internal.hostname + "saveExperiment",
          data: message
          headers:
              "Content-Type": "application/json; charset=UTF-8"
        ).then ((output) ->
          console.log "Data successfully saved"
        ), (error) ->
          console.log "some error"
      internal.sendMessage(configuration,callback)
    
    getGLD: (gld_input) ->
      console.log "sadfsafsafa"
      console.log gld_input
      request.post(internal.hostname + "gld",
        handleAs: "json"
        data:  JSON.stringify gld_input
        headers:
            "Content-Type": "application/json; charset=UTF-8"
      ).then ((output) ->
        console.log output
        console.log "Data successfully loaded from database"
      ), (error) ->
        console.log "some error"  
    
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
          textFileAsBlob = new Blob [output], type:'text/plain'
          downloadLink = document.createElement "a"
          downloadLink.download = "sample.aq21"
          downloadLink.innerHTML = "Download File"
          downloadLink.href = window.URL.createObjectURL textFileAsBlob
          downloadLink.onclick = (event) ->
            document.body.removeChild(event.target)
          downloadLink.style.display = "none"
          document.body.appendChild downloadLink
   
          downloadLink.click()
          console.log output
        ), (error) ->
          console.log "Couldn't run experiment"
          
      internal.sendMessage(configuration,callback) 
      
    convert :  
      AQ21: (file_content) ->  
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