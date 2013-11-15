define ["custom/data/attributes"], (attributeUtils) ->
  internal = 
    partialRight : ( fn , args) ->
        aps = Array.prototype.slice
        args = aps.call arguments, 1
        () ->
          fn.apply this, aps.call( arguments ).concat args  

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
  
  
  # public
  get : (attribute, domain_query) ->
    baseDomain = attributeUtils.getBaseDomain attribute, domain_query
    baseParameters = attributeUtils.getBaseParameters attribute, domain_query
    
    a = internal.partialRight internal[baseDomain], attribute.name
    internal.partialRight a, baseParameters