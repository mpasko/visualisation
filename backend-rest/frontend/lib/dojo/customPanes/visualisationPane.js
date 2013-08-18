/*global define window*/
/*global define console*/

define([],	function(){
    return {
        setup: function() {
            // setting up LaTEX support with MathJax
            MathJax.Hub.Config({ tex2jax: {inlineMath: [["$$","$$"]] }, imageFont:null});
            
            // little function for making updates more handy - will probably migrate to its own file
            window.UpdateMath = function (id, TeX) {
                document.getElementById(id).innerHTML = TeX;
                MathJax.Hub.Queue(["Typeset", MathJax.Hub, id]);
            };
            
            
        }
    };
});

