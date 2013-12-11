define [
   "dojo/dom-construct", "dojo/_base/window", "dijit/registry", "dojo/topic"
], (domConstruct, win, registry, topic) ->
  internal =
    loadFile : (f) ->
      extension = if f.fileName? then f.fileName else f.name
      reader = new FileReader()
      reader.onload = (e) ->
        converting = (file_content) ->
          if extension.match(/\.(aq21|AQ21|a21|q21)$/)
            topic.publish "convert AQ21", file_content
          else if extension.match(/\.(csv)$/)
            topic.publish "convert CSV", file_content
          else
            humane.log "Not a valid extension"

        converting  e.target.result

      reader.readAsText f
  module = 
    createFileLoader : ->
        loader = domConstruct.create "input", 
          style: 
            display: "none" 
          type: "file"
          id: "load_files"
        , win.body()
        
        loader.addEventListener 'change', 
          (evt) ->
            internal.loadFile evt.target.files[0]
        , false
        
        registry.byId("file_button").on 'click', 
          ->
            loader.click()


  module