/* global define window */
/* global define console */

function isEmpty(str) {
    return (!str || 0 === str.length);
}

window.define([], function() {
    return {
        renderOutput : function(id, hypotheses) {
                console.log("The server returned: ", hypotheses);
                var output = "";
                var dict = {">" : ">", "<" : "<", ">=":"\\geq","<=":"\\leq","=":"="};
				var renderSelector = function(selector) {
					if(!isEmpty(selector.range_begin)&&!isEmpty(selector.range_end)){
						return ["(",selector.name,"\\in\\langle",selector.range_begin,"\\ ",selector.range_end,"\\rangle",")"].join(" ");
					} else if (!isEmpty(selector.set_elements)){
						var index = 0;
						output = ["(",selector.name,"\\in\\{"];
						selector.set_elements.forEach(function(element){
							output = output.join(element);
							++index;
							if(index!=selector.set_elements.length){
								output = output.join(",\\ ");
							}
						});
						return output.join("\\})").join(" ");
					}else{
						return ["(",selector.name,dict[selector.comparator],selector.value,")"].join(" ");
					}
                };
                hypotheses.forEach(function (hyp) {
                    var out = hyp.classes[0];
                    var second = renderSelector(out);
                    
                    hyp.rules.forEach(function(rule) {
                        console.log(rule.selectors);
                        var first = rule.selectors.map(renderSelector).join(" \\land ");
                        output = output.concat(["$$",first,"\\implies",second,"$$"].join(" "));
                        
                    });
                });
                UpdateMath("MathOutput", output);
                
        }   
    };
});
