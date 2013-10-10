define [
   "custom/backend", "humane-js/humane"
], (backend, humane) ->
  eventHandler: (evt) ->
    f = evt.target.files[0]
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