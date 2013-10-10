require [
  "dojo/parser", 
  "custom/splash","custom/grid",
  "MathJax/MathJax.js?config=TeX-AMS-MML_SVG-full", "d3/d3"
], (parser, splash, grid, attr) ->
  require [ "nvd3/nv.d3" ], ->
    parser.parse()
    splash.play()
