define [], () ->
  nominal: (object, value, node, options, parameters, attr_name) ->
          div = document.createElement "div"
          div.className = "renderedCell"
          div.innerHTML = value
          div
      
  linear: (object, value, node, options, parameters, attr_name) ->
      div = document.createElement "div"
      div.className = "renderedCell"
      div.innerHTML = value
      div

  continuous: (object, value, node, options, parameters, attr_name) ->
      div = document.createElement "div"
      div.className = "renderedCell"
      min = parseFloat parameters[0]
      max = parseFloat parameters[1]
      val = Math.round 100 * ((parseFloat value) - min) / (max - min)
      if val < 33 then color = "#3FFF00"
      else if val < 66 then color = "#FFD300"
      else color = "#ED1C24"
      div.style.backgroundColor = color
      div.style.width = val + "%"

      div.style.textAlign = "center"
      div.style.borderRadius = "15px"

      div.innerHTML = value

      div