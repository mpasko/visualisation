require(["dojo/parser", "custom/splash", "custom/grid", "humane-js/humane", "MathJax/MathJax.js?config=TeX-AMS-MML_SVG-full"], function(parser, splash, grid, humane) {
  parser.parse();
  return splash.play();
});
