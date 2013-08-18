/* global define window */
/* global define console */

window.define([], function() {
    return {
        renderOutput : function(id, hypotheses) {
                console.log("The server returned: ", hypotheses);
                var output = "";
                var dict = {">" : ">", "<" : "<", ">=":"\\geq","<=":"\\leq","=":"="};
                hypotheses.forEach(function (hyp) {
                    var out = hyp.classes[0];
                    var second = ["(",out.name,dict[out.comparator],out.value,")"].join(" ");
                    
                    hyp.rules.forEach(function(rule) {
                        console.log(rule.selectors);
                        var first = rule.selectors.map(function(selector) {
                            return ["(",selector.name,dict[selector.comparator],selector.value,")"].join(" ");
                        }).join(" \\land ");
                        output = output.concat(["$$",first,"\\implies",second,"$$"].join(" "));
                        
                    });
                });
                UpdateMath("MathOutput", output);
                
        }   
    };
});
