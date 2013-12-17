define [
  "dojo/dom", "dijit/registry"
], (dom, registry) ->
  # private
  internal = 
    isEmpty : (str) ->
      not str or 0 is str.length
     
    dict :
      ">": ">"
      "<": "<"
      ">=": "\\geq"
      "<=": "\\leq"
      "=": "="
      
    renderSelector : (selector) ->
      if not @isEmpty(selector.range_begin) and not @isEmpty(selector.range_end)
        ["(", selector.name, "\\in\\langle", selector.range_begin, ",\\ ", selector.range_end, "\\rangle", ")"].join " "
      else unless @isEmpty(selector.set_elements)
        index = 0
        output = ["(", selector.name, "\\in\\{"]
        selector.set_elements.forEach (element) ->
          output.push(element)
          ++index
          output.push(",\\ ") unless index is selector.set_elements.length

        output.push("\\})")
        output.join " "
      else
        ["(", selector.name, @dict[selector.comparator], selector.value, ")"].join " "  
          
    renderRule : (rule, consequent) ->
      antecedent = (@renderSelector rule for rule in rule.selectors).join " \\land "
      ["$$", antecedent, "\\implies", consequent, "$$"].join "\n"
        
    renderHypotheses : (hyp) ->
      consequent = @renderSelector hyp.classes[0]
      (@renderRule(rule, consequent) for rule in hyp.rules).join "\n"
      
    updateMath : (id, TeX) ->
      dom.byId(id).innerHTML = TeX
      MathJax.Hub.Queue(["Typeset", MathJax.Hub, id])
      
    convertToLaTex : (hypotheses) ->
      (internal.renderHypotheses(hyp) for hyp in hypotheses).join " "
           
  # public
  module = 
    setup : ->
      MathJax.Hub.Config(
        tex2jax: 
          inlineMath: [["$$","$$"]] 
        imageFont:null
      )
    
    update : (results) ->
      hypotheses = results["outputHypotheses"]
      if hypotheses.length == 0
        return
      console.log "The server returned: ", hypotheses
      text = internal.convertToLaTex hypotheses 
      internal.updateMath "MathOutput", text
      textbox = registry.byId "LatexCode"    
      textbox.set "value", text
      
  
  module
  