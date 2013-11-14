define [
   "custom/backend",  "dojo/dom","dojo/dom-construct", "dojo/_base/window","dojo/topic"
], (backend, dom,  domConstruct, win, topic) ->
  eventHandler: (evt) ->
    f = evt.target.files[0]
    extension = if f.fileName? then f.fileName else f.name
    reader = new FileReader()
    reader.onload = (e) ->
      converting = (file_content) ->
        topic.publish "experiment raw text loaded", file_content
        if extension.match(/\.(aq21|AQ21|a21|q21)$/)
          backend.convertAQ21 file_content
        else if extension.match(/\.(csv)$/)
          backend.convertCSV file_content
        else
          humane.log "Not a valid extension"
        
      converting  e.target.result

    reader.readAsText f
  
  handleDragOver : (evt) ->
    evt.stopPropagation()
    evt.preventDefault()
    evt.dataTransfer.dropEffect = 'copy'
    
  handleFileSelect :(evt) ->
    evt.stopPropagation()
    evt.preventDefault()

    f = evt.dataTransfer.files[0]

    extension = if f.fileName? then f.fileName else f.name
    console.log URL.createObjectURL f
    reader = new FileReader()
    reader.onload = (e) ->
      converting = (file_content) ->
        if extension.match(/\.(aq21|AQ21|a21|q21)$/)
          backend.convertAQ21 file_content
        else
          humane.log "Not a valid AQ21 extension"
        
      converting  e.target.result

    reader.readAsText f

  createFileLoader : ->
      domConstruct.create "input", 
        style: 
          display: "none" 
        type: "file"
        id: "load_files"
      , win.body()
      
      dom.byId("load_files").addEventListener 'change', @eventHandler, false
      dropZone = dom.byId 'drop_zone'

      dropZone.addEventListener 'dragover', @handleDragOver, false
      dropZone.addEventListener 'drop', @handleFileSelect, false
      
      tmp = ->
        dom.byId("load_files").click()
      dropZone.addEventListener 'click', tmp, false