define [
  "dojo/topic", "dijit/registry", "custom/file"
], (topic, registry, file) ->
  # private
  internal = 
    textProvided : (text) ->
      registry.byId("loaded_text").set 'value', text.split("\n")[..30].join("\n")
      registry.byId("accordion").selectChild registry.byId("raw_aq21_text"), true
      
  # public 
  module = 
    setup : ->
      file.createFileLoader()
      topic.subscribe "experiment raw text loaded",  internal.textProvided
        
  module