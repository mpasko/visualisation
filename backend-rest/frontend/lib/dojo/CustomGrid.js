/*global define window*/
window.define(
	[
		"dojo/_base/declare", "dgrid/OnDemandGrid", "dgrid/CellSelection", "dgrid/Keyboard", "dgrid/extensions/ColumnResizer", 
		"dgrid/Selection","dgrid/extensions/DijitRegistry"
	],
	function(declare, OnDemandGrid, CellSelection, Keyboard, ColumnResizer, Selection, DijitRegistry) {
		return declare([OnDemandGrid,  Selection, Keyboard, ColumnResizer, DijitRegistry]);
});
