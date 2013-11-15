define(["dojo/aspect", "dojo/store/Memory", "dijit/registry", "dijit/layout/ContentPane", "dijit/form/TextBox", "dgrid/editor", "custom/backend", "custom/grid", "dojo/on"], function(aspect, Memory, registry, ContentPane, TextBox, editor, backend, grid, dojo_on) {
  var internal, module;
  internal = {
    params_store: new Memory(),
    removeTab: function(tab) {
      registry.byId("runTabs").removeChild(tab);
      return registry.byId(tab.id).destroy();
    },
    createTab: function(tab, params_store) {
      var contentPane, params_grid;
      contentPane = new ContentPane({
        title: tab
      });
      params_grid = new grid.onDemand({
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
        store: params_store,
        query: {
          parent: tab
        }
      });
      contentPane.addChild(params_grid);
      return registry.byId("runTabs").addChild(contentPane);
    }
  };
  module = {
    createViewFromData: function(input) {
      var child, conf_grid, parameters, run, runs, x, _i, _j, _len, _len1, _ref, _ref1;
      conf_grid = registry.byId("runs");
      runs = input.runsGroup;
      parameters = runs.runs.reduce((function(x, y) {
        return x.concat(y.runSpecificParameters);
      }), []);
      parameters = parameters.concat(runs.globalLearningParameters);
      internal.params_store.setData(parameters);
      internal.runs_store = new Memory({
        data: (function() {
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
        })()
      });
      conf_grid.set("store", internal.runs_store);
      _ref = registry.byId("runTabs").getChildren();
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        child = _ref[_i];
        internal.removeTab(child);
      }
      _ref1 = internal.runs_store.query({});
      for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
        run = _ref1[_j];
        internal.createTab(run.id, internal.params_store);
      }
      return conf_grid.refresh();
    },
    createDataFromView: function(collect) {
      var input, parametersStore, runNames, runsStore, x;
      runsStore = internal.runs_store;
      parametersStore = internal.params_store;
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
      var conf_grid;
      conf_grid = grid.onDemand({
        columns: [
          {
            field: "id",
            label: "Run"
          }, editor({
            field: "selected",
            label: "Selected",
            autoSave: true
          }, 'checkbox')
        ]
      }, "runs");
      dojo_on(registry.byId("run_button"), "click", backend.runExperiment);
      return dojo_on(registry.byId("export_button"), "click", backend.runExport);
    }
  };
  return module;
});
