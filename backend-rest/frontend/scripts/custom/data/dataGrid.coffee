define ["dijit/registry","dojo/dom-construct","custom/grid", "dgrid/editor", "custom/data/attributes","dijit/form/TextBox"], 
(registry,domConstruct,grid,editor,utils,TextBox) ->
  # private
  internal = 
    renderers : {}
    get : (attribute, domain_query) ->
      baseDomain = utils.getBaseDomain attribute, domain_query
      baseParameters = utils.getBaseParameters attribute, domain_query
      
      internal.partialRight @renderers[baseDomain], baseParameters
    
    partialRight : ( fn , args) ->
        aps = Array.prototype.slice
        args = aps.call arguments, 1
        () ->
          fn.apply this, aps.call( arguments ).concat args  

    plainCellRenderer : (object, value, node, options, parameters) ->
        div = document.createElement "div"
        div.className = "renderedCell"
        div.innerHTML = value
        div

    numberCellRenderer: (object, value, node, options, parameters) ->
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

  internal.renderers = 
    nominal : internal.plainCellRenderer
    linear : internal.plainCellRenderer
    integer : internal.numberCellRenderer
    continuous : internal.numberCellRenderer
  
  # public 
  module = 
    setup : (stores) ->
      internal.stores = stores
      
    update : () ->
       mapping = utils.getMapping internal.stores, ["linear", "nominal","integer", "continuous"]
       
       columns = (
          (
            field : item.field
            label : item.attribute.name
            autoSave : true
            renderCell : internal.get item.attribute, internal.stores.domains.query(name: item.attribute.domain)
          )  for item in mapping
       )
       
       registry.byId("events").destroyDescendants false
       domConstruct.create("div", 
        id: "datagrid"
        style :"height : 100%;"
       , "events")
       
       eventsGrid = new grid.paginated(
         store : internal.stores.events, 
         columns : editor(column, TextBox, "click") for column in columns
         pagingLinks: 3
         firstLastArrows: true
         rowsPerPage : 20
         pageSizeOptions: [20, 50, 100]
         , "datagrid")

       eventsGrid.startup()
        
  module