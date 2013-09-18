define [
  "dojo/topic","dojo/store/Memory",
  "custom/visualisation/latex"
  ], (topic, Memory, latex) ->
  internal = null
  
  module = 
    setup : ->
      latex.setup()
      
      topic.subscribe "visualise results", (results)->
        latex.renderMath "MathOutput", results