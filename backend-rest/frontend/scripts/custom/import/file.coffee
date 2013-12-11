define [
   "custom/backend",  "dojo/dom","dojo/dom-construct", "dojo/_base/window", "dijit/registry"
], (backend, dom,  domConstruct, win, registry) ->
  internal =
    loadFile : (f) ->
      extension = if f.fileName? then f.fileName else f.name
      reader = new FileReader()
      reader.onload = (e) ->
        converting = (file_content) ->
          if extension.match(/\.(aq21|AQ21|a21|q21)$/)
            backend.convert.AQ21 file_content
          else if extension.match(/\.(csv)$/)
            backend.convert.CSV file_content
          else
            humane.log "Not a valid extension"

        converting  e.target.result

      reader.readAsText f
      
    eventHandler: (evt) ->
      internal.loadFile evt.target.files[0]

  module = 
    createFileLoader : ->
        domConstruct.create "input", 
          style: 
            display: "none" 
          type: "file"
          id: "load_files"
        , win.body()

        dom.byId("load_files").addEventListener 'change', internal.eventHandler, false
        registry.byId("file_button").on 'click', 
          ->
            dom.byId("load_files").click()


  module