define [
  "dojo/store/Memory",
  "custom/visualisation/latex",
  "custom/visualisation/raw",
  "custom/visualisation/gld"
  ], (Memory, latex, raw, gld) ->
  internal =
    stores:
      output : new Memory()
    visualisations: [
      raw,
      latex,
      gld
    ]
    results : null
  
  module = 
    update: (results)->
        internal.stores.output.setData results["outputHypotheses"]
        item.update results for item in internal.visualisations
        
        
    setup : ->
      latex.setup()
      raw.setup()
      gld.setup()
    
        
      
        