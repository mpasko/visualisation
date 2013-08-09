/*global define window*/
/*global define console*/

window.define([],	function() {
		return {
            renderOutput : function(id, hypotheses) {
                console.log("The server returned: ", hypotheses);
            }
        };
});

