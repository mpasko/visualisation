define [], () ->
   partialRight : ( fn , args) ->
      aps = Array.prototype.slice
      args = aps.call arguments, 1
      () ->
        fn.apply this, aps.call( arguments ).concat args 