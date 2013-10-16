define [
  "dojo/dom", "dijit/registry",
  "dojo/topic","dojo/store/Memory"
], (dom, registry, topic, Memory) ->

  # private
  internal = 
    output_store : new Memory()  
    
  # public
  module = 
    setup : ->
      console.log "Raw module init"
      topic.subscribe "raw format", (results)->
        internal.output_store.setData results
        rawtextbox = registry.byId "RawAq21"   
        rawtextbox.set "value", results
  module