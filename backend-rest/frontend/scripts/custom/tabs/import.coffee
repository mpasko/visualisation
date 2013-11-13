define [
  "dojo/dom","dojo/dom-construct", "dojo/_base/window","dojo/topic",
  "dijit/registry", "custom/file","dojo/on"
], (dom,  domConstruct, win, topic, registry, file,  dojo_on) ->
  # private
  internal = 
    createFileLoader : ->
      domConstruct.create "input", 
        style: 
          display: "none" 
        type: "file"
        id: "load_files"
      , win.body()
      
      dom.byId("load_files").addEventListener 'change', file.eventHandler, false
      dropZone = dom.byId 'drop_zone'

      dropZone.addEventListener 'dragover', file.handleDragOver, false
      dropZone.addEventListener 'drop', file.handleFileSelect, false
      
      tmp = ->
        dom.byId("load_files").click()
      dropZone.addEventListener 'click', tmp, false
      
  # public 
  module = 
    setup : ->
      internal.createFileLoader()
      
      topic.subscribe "experiment raw text",  (text) ->
        registry.byId("loaded_text").set 'value', text
        registry.byId("accordion").selectChild registry.byId("raw_aq21_text"), true

  module