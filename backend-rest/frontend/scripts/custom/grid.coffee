define [
  "dojo/_base/declare", 
  "dgrid/Grid", "dgrid/Keyboard", "dgrid/extensions/ColumnResizer", 
  "dgrid/Selection", "dgrid/extensions/DijitRegistry","dgrid/extensions/Pagination", "dgrid/OnDemandGrid"
],
    (declare, Grid, Keyboard, ColumnResizer, Selection, DijitRegistry,Pagination,OnDemandGrid) ->
      paginated : declare [Grid, Selection, Keyboard, ColumnResizer, DijitRegistry,Pagination]
      onDemand : declare [OnDemandGrid, Selection, Keyboard, ColumnResizer, DijitRegistry]
      simple : declare [Grid, Selection, Keyboard, ColumnResizer, DijitRegistry]