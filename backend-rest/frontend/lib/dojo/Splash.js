/*global define window*/
/*global define console*/
/*global define FileReader*/
/*global define attributesStore*/
/*global define parametersStore*/
/*global define eventsStore*/
/*global define statisticsStore*/
/*global define runsStore*/

define([
	 "dojo/dom","dijit/registry", "dojo/_base/declare","dojo/dom-style","dojo/_base/fx"
],function( dom, registry, declare,domStyle, fx){
	return {
		play: function  (name) {
			domStyle.set(name, "opacity", "1");
			var fadeArgs = { node: name, duration : 1000 };
		    var animation = fx.fadeOut(fadeArgs);
		    animation.onEnd = function(){domStyle.set(name, {   display: "none"  });};
		    animation.play();
		}
	};
});

