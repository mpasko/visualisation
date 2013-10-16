define(["dojo/_base/declare", "dgrid/Grid", "dgrid/Keyboard", "dgrid/extensions/ColumnResizer", "dgrid/Selection", "dgrid/extensions/DijitRegistry", "dgrid/extensions/Pagination", "dgrid/OnDemandGrid"], function(declare, Grid, Keyboard, ColumnResizer, Selection, DijitRegistry, Pagination, OnDemandGrid) {
  return {
    paginated: declare([Grid, Selection, Keyboard, ColumnResizer, DijitRegistry, Pagination]),
    onDemand: declare([OnDemandGrid, Selection, Keyboard, ColumnResizer, DijitRegistry])
  };
});
