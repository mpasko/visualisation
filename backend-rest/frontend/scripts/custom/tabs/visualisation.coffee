define [
  "dojo/topic","dojo/store/Memory",
  "custom/visualisation/latex"
  ], (topic, Memory, latex) ->
  internal = 
    output_store : new Memory()
  
  module = 
    setup : ->
      latex.setup()
      
      topic.subscribe "visualise results", (results)->
        internal.output_store.setData results
        latex.renderMath "MathOutput", results