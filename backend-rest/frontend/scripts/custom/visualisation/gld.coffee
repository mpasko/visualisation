define [
  "dojo/dom", "dijit/registry", "dojo/topic"
], (dom, registry, topic) ->
  # private
  internal = 
    results : null
           
  # public
  module = 
    setup : ->
      registry.byId("load_gld").on "click",
        ->
          topic.publish "create gld diagram", data : internal.results, ratio_importance : parseFloat registry.byId("gldParameter").value
    
    update : (results) ->
      internal.results = results
      console.log "aaaa"
      
  
  module
  