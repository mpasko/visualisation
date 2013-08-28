window.require(["dojo/dom", "dojo/ready", "dijit/layout/ContentPane", "File","dojo/parser","dgrid/GridFromHtml", 
                "dgrid/OnDemandList","dgrid/editor","dojo/_base/declare", "dgrid/Keyboard",
                "dgrid/extensions/ColumnResizer", "dgrid/Selection", "dgrid/extensions/DijitRegistry"],
    function(dom, ready, ContentPane,  file,parser,GridFromHtml, 
                OnDemandList,editor,declare, Keyboard, 
                ColumnResizer, Selection, DijitRegistry) {
        ready(function() {
            window.dgrid = {
                GridFromHtml: declare([GridFromHtml, OnDemandList,Selection, Keyboard, ColumnResizer, DijitRegistry]),
                editor: editor
            };
            
            dom.byId("load_files").addEventListener('change', file.eventHandler, false);
            parser.parse();
        });
});
