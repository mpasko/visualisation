define [
  "dojo/store/Memory",
  "custom/visualisation/latex",
  "custom/visualisation/raw",
  ], (Memory, latex, raw) ->
  internal =
    stores:
      output : new Memory()
    visualisations: [
      raw,
      latex
    ]
  
  module = 
    update: (results)->
        internal.stores.output.setData results["outputHypotheses"]
        item.update results for item in internal.visualisations

    setup : ->
      latex.setup()
      raw.setup()
      
    
        
      
        