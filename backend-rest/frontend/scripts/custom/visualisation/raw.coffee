define [
  "dojo/dom", "dijit/registry", "dojo/store/Memory"
], (dom, registry, Memory) ->
  # private
  internal = 
    {}
    
  # public
  module = 
    update : (results)->
        rawtextbox = registry.byId "RawAq21"   
        rawtextbox.set "value", results["raw_aq21"]
  
    setup : ->
      {}
        
  module