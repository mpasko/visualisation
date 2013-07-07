/*global define window*/
/*global define dojo*/
window.require(["dojo/dom-style","dojo/parser","dojo/_base/fx" ], 
	function(domStyle, parser,fx) {
		dojo.addOnLoad(function()
		{
			domStyle.set("splash", "opacity", "1");
			domStyle.set("load_files", { display: "none"  });
			var fadeArgs = { node: "splash", duration : 1000 };
		    var animation = fx.fadeOut(fadeArgs);
		    animation.onEnd = function(){domStyle.set("splash", {   display: "none"  });};
		    animation.play();
		});
		
});