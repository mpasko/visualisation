require(["dojo/parser", "custom/splash", "custom/grid", "MathJax/MathJax.js?config=TeX-AMS-MML_SVG-full"], function(parser, splash, grid, attr) {
  parser.parse();
  return splash.play();
});
