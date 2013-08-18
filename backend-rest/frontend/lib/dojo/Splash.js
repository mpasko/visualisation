/* global define window */
/* global define console */

define(["dojo/dom-style", "dojo/_base/fx", "dojo/fx"], function(domStyle, fx,
				coreFx) {
			return {
				play : function(names) {
					var anims = names.map(function(name) {
								domStyle.set(name, "opacity", "1");
								var fadeArgs = {
									node : name,
									duration : 1000
								};
								var animation = fx.fadeOut(fadeArgs);
								animation.onEnd = function() {
									domStyle.set(name, {
												display : "none"
											});
								};
								return animation;
							});
					coreFx.chain(anims).play();
				}
			};
		});
