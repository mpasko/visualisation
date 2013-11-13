define([], function() {
  return {
    partialRight: function(fn, args) {
      var aps;
      aps = Array.prototype.slice;
      args = aps.call(arguments, 1);
      return function() {
        return fn.apply(this, aps.call(arguments).concat(args));
      };
    }
  };
});
