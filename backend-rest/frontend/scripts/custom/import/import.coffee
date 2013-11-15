define [
  "dijit/registry", "custom/import/file"
], (registry, file) ->
  # private
  internal = 
    {}
      
  # public 
  module = 
    textProvided : (text) ->
      registry.byId("loaded_text").set 'value', text.split("\n")[..30].join("\n")
      registry.byId("accordion").selectChild registry.byId("raw_aq21_text"), true
      
    setup : ->
      file.createFileLoader()
        
  module