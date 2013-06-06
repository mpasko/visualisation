
function readSingleFile(evt) {
	//Retrieve the first (and only!) File from the FileList object
    var f = evt.target.files[0]; 

	if (f) {
		var r = new FileReader();
		r.onload = function(e) { 
			var csv = e.target.result;
			jQuery('#Response').empty();
			convertFromCSV(csv, function(result){
				//On success
				jQuery('#Response').append(result.replace(/\n/g, "<br>"));
			}, function(){
				//On error
				jQuery('#Response').append("Request failed");
				alert("Request failed");
			});
		}
		r.readAsText(f);
		
		return false;
	} else { 
		alert("Failed to load file");
	}
}

jQuery.noConflict();
jQuery(document).ready(function() {
	var query = 
{
  "runsGroup" : {
    "runs" : [ {
      "runSpecificParameters" : [ {
        "name" : "Mode",
        "value" : "TF"
      }, {
        "name" : "Consequent",
        "value" : "[class=c1]"
      }, {
        "name" : "Ambiguity",
        "value" : "IncludeInPos"
      }, {
        "name" : "Trim",
        "value" : "Optimal"
      }, {
        "name" : "Compute_alternative_covers",
        "value" : "True"
      }, {
        "name" : "Maxstar",
        "value" : "1"
      }, {
        "name" : "Maxrule",
        "value" : "10"
      } ],
      "name" : "Run_c1"
    }, {
      "runSpecificParameters" : [ {
        "name" : "Mode",
        "value" : "TF"
      }, {
        "name" : "Consequent",
        "value" : "[class=c2]"
      }, {
        "name" : "Ambiguity",
        "value" : "IncludeInPos"
      }, {
        "name" : "Trim",
        "value" : "Optimal"
      }, {
        "name" : "Compute_alternative_covers",
        "value" : "False"
      }, {
        "name" : "Maxstar",
        "value" : "1"
      }, {
        "name" : "Maxrule",
        "value" : "1"
      } ],
      "name" : "Run_c2"
    }, {
      "runSpecificParameters" : [ {
        "name" : "Mode",
        "value" : "PD"
      }, {
        "name" : "Consequent",
        "value" : "[class=*]"
      }, {
        "name" : "Ambiguity",
        "value" : "IncludeInPos"
      }, {
        "name" : "Trim",
        "value" : "Optimal"
      }, {
        "name" : "Compute_alternative_covers",
        "value" : "False"
      }, {
        "name" : "Maxstar",
        "value" : "1"
      }, {
        "name" : "Maxrule",
        "value" : "1"
      } ],
      "name" : "Run_All_in_PD"
    }, {
      "runSpecificParameters" : [ {
        "name" : "Mode",
        "value" : "PD"
      }, {
        "name" : "Consequent",
        "value" : "[class=c1][length<=40]"
      }, {
        "name" : "Ambiguity",
        "value" : "IncludeInPos"
      }, {
        "name" : "Trim",
        "value" : "Optimal"
      }, {
        "name" : "Compute_alternative_covers",
        "value" : "False"
      }, {
        "name" : "Maxstar",
        "value" : "1"
      }, {
        "name" : "Maxrule",
        "value" : "1"
      } ],
      "name" : "Run_Multi-head"
    } ],
    "globalLearningParameters" : [ ]
  },
  "attributes" : [ {
    "name" : "background",
    "domain" : "color",
    "parameters" : ""
  }, {
    "name" : "number",
    "domain" : "linear",
    "parameters" : "{ 0, 1, 2 }"
  }, {
    "name" : "length",
    "domain" : "continuous",
    "parameters" : "0, 200"
  }, {
    "name" : "class",
    "domain" : "nominal",
    "parameters" : "{c1, c2}"
  } ],
  "domains" : [ {
    "name" : "color",
    "subdomain" : "nominal",
    "parameters" : "{red, green, blue}"
  } ],
  "events" : [ {
    "values" : [ "red", "1", "34.6", "c1" ]
  }, {
    "values" : [ "green", "0", "2.45", "c2" ]
  }, {
    "values" : [ "red", "1", "33.0", "c1" ]
  }, {
    "values" : [ "blue", "0", "33.5", "c2" ]
  } ]
};

	jQuery('#Request').append(JSON.stringify(query));
	
	jQuery('#Button').click(function(){
		jQuery('#Response').empty();
		invokeAQ21(query, function(result){
			//On success
			jQuery('#Response').append(result.replace(/\n/g, "<br>"));
		}, function(){
			//On error
			jQuery('#Response').append("Request failed");
		});
		return false;
	});
	
	document.getElementById('fileinput')
	.addEventListener('change', readSingleFile, false);
});