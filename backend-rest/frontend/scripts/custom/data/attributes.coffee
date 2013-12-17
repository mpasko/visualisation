define [], () ->
  hasDefaultDomain : (attribute) ->
    attribute.domain in ["linear", "nominal", "integer", "continuous"]

  getBaseDomain : (attribute, domain_query) ->
    if @hasDefaultDomain(attribute) then attribute.domain
    else domain_query[0].domain
    
  getBaseParameters : (attribute, domain_query) ->
    if @hasDefaultDomain(attribute) then parameters = attribute.parameters
    else parameters = domain_query[0].parameters
    
    tmp = (parameter.trim() for parameter in /([^{}]+)/.exec(parameters)[1].split " ")

    
    (if parameter.slice(-1) == "," then parameter.slice(0, - 1)  else parameter) for parameter in tmp
    
  extractAttributeDescription : (attribute, domain_query) ->
      domain : attribute.domain
      baseDomain : @getBaseDomain attribute, domain_query
      parameters : @getBaseParameters attribute, domain_query
       
  getContinuousValues : (params) ->
     [
         id : "Minimum"
         value : params[0]
       ,
         id : "Maximum"
         value : params[1]
     ]

  getDiscreteValues : (params) -> 
    arr = []
    i = 0
    while i < params.length
      arr.push
        id : i
        value : params[i]
      i++
    arr
    
  pad : (n, width) ->
    n_str = String(n)
    while n_str.length != width
      n_str = '0' + n_str
    n_str
  
   getMapping : (stores, arr) ->
    attributes = stores.attr.query {}
    selected_attributes = stores.attr.query {selected : true}
    
    discretes = (attribute for attribute in selected_attributes when (@getBaseDomain attribute, stores.domains.query(name: attribute.domain)) in arr)
    width = String(attributes.length).length
    
    (
          (
            field : 'attribute' + @pad(attributes.indexOf(attribute)+1, width)
            parameters : (@getBaseParameters attribute, stores.domains.query(name: attribute.domain))
            attribute : attribute
          )  for attribute in discretes
    )
   
   getDiscreteMapping : (stores, arr) ->
    attributes = stores.attr.query {}
    selected_attributes = stores.attr.query {}
    
    discretes = (attribute for attribute in selected_attributes when (@getBaseDomain attribute, stores.domains.query(name: attribute.domain)) in arr)
    width = String(attributes.length).length
    
    (
          (
            field : 'attribute' + @pad(attributes.indexOf(attribute)+1, width)
            parameters : (@getBaseParameters attribute, stores.domains.query(name: attribute.domain))
            attribute : attribute
          )  for attribute in discretes
    )    