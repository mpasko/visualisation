require(["dojo/parser", "custom/splash", "custom/grid", "MathJax/MathJax.js?config=TeX-AMS_HTML", "d3/d3"], function(parser, splash, grid, attr) {
  return require(["nvd3/nv.d3"], function() {
    parser.parse();
    return splash.play();
  });
});
