define([
    "dojo/_base/declare",
    "dijit/Dialog",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/text!./templates/GridView.html",
    "dojo/dom",
    "dijit/registry",
    "classes/input/File",
    "dgrid/CellSelection", "dgrid/Keyboard", "dgrid/extensions/ColumnResizer","dgrid/OnDemandGrid","dgrid/extensions/DijitRegistry"
], function (declare, Dialog, TemplatedMixin, _WidgetsInTemplateMixin, template, dom, registry, File,
CellSelection, Keyboard, ColumnResizer, OnDemandGrid, DijitRegistry) 
{
		// Create a new class named "mynamespace.MyClass"
		return declare("classes.dialogs.GridView", [Dialog, TemplatedMixin, _WidgetsInTemplateMixin], {
		// The constructor
	    constructor: function(args){
	        declare.safeMixin(this,args);
	        
	    },
        templateString:template,
        
        postCreate:function () {
        	this.inherited(arguments);
        }        
    });
});

