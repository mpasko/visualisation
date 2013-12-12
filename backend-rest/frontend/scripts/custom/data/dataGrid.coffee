define ["dijit/registry","dojo/dom-construct","custom/grid", "dgrid/editor", "custom/data/attributes","dijit/form/TextBox"], 
(registry,domConstruct,grid,editor,attributeUtils,TextBox) ->
  # private
  internal = 
    get : (attribute, domain_query) ->
      baseDomain = attributeUtils.getBaseDomain attribute, domain_query
      baseParameters = attributeUtils.getBaseParameters attribute, domain_query

      a = @partialRight @[baseDomain], attribute.name
      
      internal.partialRight a, baseParameters
    
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
        
    integer: (object, value, node, options, parameters, attr_name) ->
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
  module = 
    setup : ->
      return
      
    update : (stores) ->
       attributes = stores.attr.query {}
       selected_attributes = stores.attr.query {selected: true}
       
       width = String(attributes.length + 1).length
       
       pad  = (n, width) ->
        n_str = String(n)
        i = 0
        while n_str.length != width
          n_str = '0' + n_str
        
        n_str

       
       columns = (
          (
            field : 'attribute' + pad(attributes.indexOf(attribute)+1, width)
            label : attribute.name
            autoSave : true
            renderCell : internal.get attribute, stores.domains.query(name: attribute.domain)
          )  for attribute in selected_attributes
       )
       
       registry.byId("events").destroyDescendants false
       domConstruct.create("div", 
        id: "datagrid"
        style :"height : 100%;"
       , "events")
       
       eventsGrid = new grid.paginated(
         store : stores.events, 
         columns : editor(column, TextBox, "click") for column in columns
         pagingLinks: 3
         firstLastArrows: true
         rowsPerPage : 20
         pageSizeOptions: [20, 50, 100]
         , "datagrid")

       eventsGrid.startup()
        
  module