(function() {
  define(["dojo/aspect", "dojo/store/Memory", "dijit/registry", "dijit/layout/ContentPane", "dijit/form/TextBox", "dgrid/editor", "custom/backend", "custom/grid", "dojo/on"], function(aspect, Memory, registry, ContentPane, TextBox, editor, backend, grid, dojo_on) {
    var internal, module;
    internal = {
      stores: {
        params: new Memory(),
        runs: new Memory()
      }
    };
    module = {
      createViewFromData: function(input) {
        var conf_grid, parameters, params_grid, runs, x;
        conf_grid = registry.byId("runs");
        params_grid = registry.byId("parameters");
        runs = input.runsGroup;
        parameters = runs.runs.reduce((function(x, y) {
          return x.concat(y.runSpecificParameters);
        }), []);
        parameters = parameters.concat(runs.globalLearningParameters);
        internal.stores.params.setData(parameters);
        internal.stores.runs.setData((function() {
          var _i, _len, _ref, _results;
          _ref = runs.runsNames.concat(["globalLearningParameters"]);
          _results = [];
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            x = _ref[_i];
            _results.push({
              id: x,
              selected: true
            });
          }
          return _results;
        })());
        conf_grid.refresh();
        return params_grid.refresh();
      },
      createDataFromView: function(collect) {
        var input, parametersStore, runNames, runsStore, x;
        runsStore = internal.stores.runs;
        parametersStore = internal.stores.params;
        runNames = (function() {
          var _i, _len, _ref, _results;
          _ref = runsStore.query({
            selected: true
          });
          _results = [];
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            x = _ref[_i];
            if (x.id !== "globalLearningParameters") {
              _results.push(x.id);
            }
          }
          return _results;
        })();
        input = {
          runsGroup: {
            runsNames: runNames,
            runs: (function() {
              var _i, _len, _results;
              _results = [];
              for (_i = 0, _len = runNames.length; _i < _len; _i++) {
                x = runNames[_i];
                _results.push({
                  name: x,
                  runSpecificParameters: parametersStore.query({
                    parent: x
                  })
                });
              }
              return _results;
            })(),
            globalLearningParameters: parametersStore.query({
              parent: "globalLearningParameters"
            })
          }
        };
        return collect(input);
      },
      setup: function() {
        var conf_grid, param_grid;
        conf_grid = new grid.onDemand({
          columns: [
            {
              field: "id",
              label: "Run"
            }, editor({
              field: "selected",
              label: "Selected",
              autoSave: true
            }, 'checkbox')
          ],
          store: internal.stores.runs
        }, "runs");
        param_grid = new grid.onDemand({
          columns: {
            name: {
              label: "Name"
            },
            value: editor({
              label: "Value",
              field: "value",
              autoSave: true
            }, TextBox, "click")
          },
          store: internal.stores.params,
          query: {
            parent: "globalLearningParameters"
          }
        }, "parameters");
        conf_grid.on("dgrid-select", function(event) {
          param_grid.set('query', {
            parent: event.rows[0].id
          });
          return param_grid.refresh();
        });
        dojo_on(registry.byId("run_button"), "click", backend.runExperiment);
        dojo_on(registry.byId("export_button"), "click", backend.runExport);
        dojo_on(registry.byId("newRunButton"), "click", function() {
          internal.stores.runs.put({
            id: registry.byId("newRunText").value,
            selected: true
          });
          conf_grid = registry.byId("runs");
          return conf_grid.refresh();
        });
        return dojo_on(registry.byId("newParameterButton"), "click", function() {
          var params_grid;
          params_grid = registry.byId("parameters");
          internal.stores.params.put({
            name: registry.byId("newParameterText").value,
            value: "value",
            parent: params_grid.query.parent
          });
          return params_grid.refresh();
        });
      }
    };
    return module;
  });

}).call(this);
