define [
  "dojo/query","dojo/dom-style","dojo/_base/fx", "dojo/fx"
 ], (query,domStyle,fx, coreFx) ->
  # private
  internal = null
  # public 
  module =
    play: () ->
       chain = coreFx.chain(
           (
              fx.animateProperty
                node: name
                properties:
                  opacity:
                    start: 1
                    end: 0
                duration: 1500
           ) for name in (name for name in query ".splash").reverse()
       )

       chain.onEnd = ->
          domStyle.set name, "display", "none" for name in query ".splash"

       chain.play()
       
  module 


    
     
     
     
