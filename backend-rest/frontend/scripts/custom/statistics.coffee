define [], ->
  # private
  internal = 
    getRawDomain: (item) ->
      if item.domain in ["linear", "nominal", "continuous"] then item.domain
      else domainsStore.query(name: item.domain)[0].domain

    getRawParameters: (item) ->
      if item.domain in ["linear", "nominal", "continuous"] then parameters = item.parameters
      else parameters = domainsStore.query(name: item.domain)[0].parameters
      /([^{}]+)/.exec(parameters)[1].split ","
    
    nominal: (name, parameters) ->
      [
        id: "Item counts"
        value: ""
      ].concat parameters.map((parameter) ->
        query = {}
        query[name] = parameter.trim()
        query.value = eventsStore.query(query).total
        query.id = parameter.trim()
        query
      )

    linear: (name, parameters) ->
      @nominal name, parameters

    continuous: (name, parameters) ->
      [
        id: "Values range"
        value: ""
      ,
        id: "min"
        value: parseFloat parameters[0]
      ,
        id: "max"
        value: parseFloat parameters[1]
      ]
  # public 
  module =
    computeStats: (item) ->
      internal[internal.getRawDomain(item)] item.name, internal.getRawParameters(item)
  
  module

  
