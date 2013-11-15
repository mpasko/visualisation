define [
   "custom/backend",  "dojo/dom","dojo/dom-construct", "dojo/_base/window"
], (backend, dom,  domConstruct, win) ->
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

    handleFileSelect :(evt) ->
      evt.stopPropagation()
      evt.preventDefault()

      internal.loadFile evt.dataTransfer.files[0]

    handleDragOver : (evt) ->
      evt.stopPropagation()
      evt.preventDefault()
      evt.dataTransfer.dropEffect = 'copy'

  module = 
    createFileLoader : ->
        domConstruct.create "input", 
          style: 
            display: "none" 
          type: "file"
          id: "load_files"
        , win.body()

        dom.byId("load_files").addEventListener 'change', internal.eventHandler, false
        dropZone = dom.byId 'drop_zone'

        dropZone.addEventListener 'dragover', internal.handleDragOver, false
        dropZone.addEventListener 'drop', internal.handleFileSelect, false

        tmp = ->
          dom.byId("load_files").click()

        dropZone.addEventListener 'click', tmp, false

  module