define(["dojo/store/Memory",
    "dojo/data/ObjectStore",
    "dijit/registry",
    "dijit/layout/ContentPane",
    "dijit/form/TextBox",
    "dgrid/editor",
    "custom/grid",
    "dojo/topic",
    "dijit/form/Button"], 
function(Memory, ObjectStore, registry, ContentPane, TextBox, editor, grid, topic, Button) {    
  var internal, module;
  internal = {
    stores: {
      params: new Memory(),
      runs: new Memory(),
      algorithms: new ObjectStore({
        objectStore: new Memory({
            data: [
                {id: "empty", label: "Please choose an algorithm..."}, 
                {id: "aq21", label: "AQ21 (Windows binary)"}, 
                {id: "j48", label: "J48 (from Weka)" }, 
                {id: "jripp", label: "JRipper (from Weka)" }
            ]})
      }),
      wholeInput: {},
      internalState: {
          algorithm: 'aq21'
      }
    },
    getConfiguration: function() {
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
      return input;
    },
    getInput: function() {
      return internal.stores.wholeInput;
    },
    deleteRun: function(object, data, cell) {
      var btnDelete;
      if (object.id === "globalLearningParameters") {
        return null;
      }
      btnDelete = new Button({
        rowId: object.id,
        label: "Remove",
        onClick: function() {
          internal.stores.runs.remove(this.rowId);
          return registry.byId("runs").refresh();
        }
      }, cell.appendChild(document.createElement("div")));
      btnDelete._destroyOnRemove = true;
      return btnDelete;
    },
    deleteParameter: function(object, data, cell) {
      var btnDelete;
      btnDelete = new Button({
        rowId: object.id,
        label: "Remove",
        onClick: function() {
          internal.stores.params.remove(this.rowId);
          return registry.byId("parameters").refresh();
        }
      }, cell.appendChild(document.createElement("div")));
      btnDelete._destroyOnRemove = true;
      return btnDelete;
    },
    setAlgorithm: function(alg) {
        internal.stores.internalState.algorithm = alg;
    },
    getAlgorithm: function() {
        return internal.stores.internalState.algorithm;
    }
  };
  module = {
    update: function(input) {
      var conf_grid, parameters, params_grid, runs, x;
      conf_grid = registry.byId("runs");
      params_grid = registry.byId("parameters");
      internal.stores.wholeInput = input;
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
    setup: function() {
      var conf_grid, param_grid;
      var algorithm_select = registry.byId("algorithm");
      conf_grid = new grid.onDemand({
        columns: [
          {
            field: "id",
            label: "Run"
          }, editor({
            field: "selected",
            label: "Selected",
            autoSave: true
          }, 'checkbox'), {
            label: "",
            field: "id",
            renderCell: internal.deleteRun
          }
        ],
        store: internal.stores.runs
      }, "runs");
      param_grid = new grid.onDemand({
        columns: [
          {
            field: "name",
            label: "Name"
          }, editor({
            label: "Value",
            field: "value",
            autoSave: true
          }, TextBox, "click"), {
            label: "",
            field: "id",
            renderCell: internal.deleteParameter
          }
        ],
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
      registry.byId("run_button").on("click", function() {
        var config = {
            'configuration':internal.getConfiguration(),
            'algorithm':internal.getAlgorithm()
        };
        return topic.publish("run experiment", config);
      });
      registry.byId("export_button").on("click", function() {
        return topic.publish("run export", internal.getConfiguration());
      });
      registry.byId("save_button").on("click", function() {
        return registry.byId("nameDialog").show();
      });
      registry.byId("save_experiment").on("click", function() {
        var desc, name, value;
        registry.byId("nameDialog").hide();
        name = registry.byId("exp_name").value;
        desc = registry.byId("exp_description").value;
        value = internal.getConfiguration();
        topic.publish("save experiment", {
          "name": name,
          "value": value,
          "description": desc
        });
      });
      registry.byId("newRunButton").on("click", function() {
        internal.stores.runs.put({
          id: registry.byId("newRunText").value,
          selected: true
        });
        return registry.byId("runs").refresh();
      });
      algorithm_select.setStore(internal.stores.algorithms);
      algorithm_select.set("value", "empty");
      algorithm_select.on("change", function() {
         var alg = algorithm_select.get("value");
         if (alg != "empty") {
            internal.stores.algorithms.fetch({
                query: {id: "empty" },
                onComplete:  function (items) {
                    internal.stores.algorithms.deleteItem(items[0]);
                }
            });
            var config = {
               "configuration":internal.getInput(),
               "algorithm":alg
            };
            topic.publish("algorithm selection", config); 
            internal.setAlgorithm(alg);
         }
      });
      return registry.byId("newParameterButton").on("click", function() {
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
