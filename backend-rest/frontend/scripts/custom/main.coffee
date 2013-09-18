require [
  "dojo/parser", 
  "custom/splash","custom/grid","humane-js/humane"
  "MathJax/MathJax.js?config=TeX-AMS-MML_SVG-full"
], (parser, splash, grid, humane) ->
  parser.parse()
  splash.play()
   
  