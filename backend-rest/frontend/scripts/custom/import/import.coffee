define [
  "dijit/registry", "custom/import/file"
], (registry, file) ->
  # private
  internal = 
    {}
      
  # public 
  module = 
    textProvided : (text) ->
      return
      
    setup : ->
      file.createFileLoader()
        
  module