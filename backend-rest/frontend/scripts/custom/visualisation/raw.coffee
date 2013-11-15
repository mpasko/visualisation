define [
  "dojo/dom", "dijit/registry", "dojo/store/Memory"
], (dom, registry, Memory) ->
  # private
  internal = 
    {}
    
  # public
  module = 
    updateTextbox : (results)->
        rawtextbox = registry.byId "RawAq21"   
        rawtextbox.set "value", results
  
    setup : ->
      {}
        
  module