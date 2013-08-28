define(["dojo/dom", "dojo/dom-style"], 
    function(dom, domStyle) {
        return {
            getSizePx : function(name) {
                var node = dom.byId(name);
                return [domStyle.get(node, "height"), domStyle.get(node, "width")];
            }
        };
});
