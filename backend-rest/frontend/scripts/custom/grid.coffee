define [
  "dojo/_base/declare", 
  "dgrid/OnDemandGrid", "dgrid/Keyboard", "dgrid/extensions/ColumnResizer", 
  "dgrid/Selection", "dgrid/extensions/DijitRegistry"
],
    (declare, OnDemandGrid, Keyboard, ColumnResizer, Selection, DijitRegistry) ->
        declare [OnDemandGrid, Selection, Keyboard, ColumnResizer, DijitRegistry]