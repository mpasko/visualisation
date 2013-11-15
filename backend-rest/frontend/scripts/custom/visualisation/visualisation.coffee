define [
  "dojo/topic","dojo/store/Memory",
  "custom/visualisation/latex",
  "custom/visualisation/raw",
  ], (topic, Memory, latex, raw) ->
  internal = 
    output_store : new Memory()
  
  module = 
    visualiseResults: (results)->
        internal.output_store.setData results["outputHypotheses"]
        raw.updateTextbox results["raw_aq21"]
        latex.renderMath "MathOutput", results["outputHypotheses"]
        
        
    setup : ->
      latex.setup()
      raw.setup()
      
    
        
      
        