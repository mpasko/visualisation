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
//	var ready = JSON.stringify(query).replace(/"/g, "'");
	var ready = JSON.stringify(query);
//	var ready = query;
	var resource = 'http://localhost:9998/jersey/aq21/postIt';
//	var resource = 'localhost:9998/jersey/aq21/debug';
	var ourType = 'application/json';
//	var ourType = 'application/xml'; Don't do that!
	jQuery('#content').append("Request<br><hr>");
	jQuery('#content').append(ready);
	jQuery('#content').append("<br>Response<br><hr>");
//	jQuery.ajaxSetup({	
//		accepts: {json: 'application/json; charset=utf-8'}
//	});
//	jQuery.post(resource, {json: ready}, function(data) {
//			jQuery('#content').append(data);
//		});
	jQuery.ajax({
		type: 'POST',
		contentType: ourType,
		dataType: 'json',
		crossDomain: true,
		url: resource,
		data: ready,
		headers: {'Access-Control-Allow-Origin:': '*'},
		cache: false,
		success: function(data) {
			jQuery('#content').append(JSON.stringify(data["outputHypotheses"][0]["content"]));
		},
		beforeSend: function(jqXHR) {
//			jqXHR.setRequestHeader("Content-type","application/json; charset=utf-8");
		},
		processData: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			jQuery('#content').append(XMLHttpRequest);
			jQuery('#content').append(errorThrown);
			jQuery('#content').append(textStatus);
			jQuery('#content').append("<br>request failed!");
		}
	});
});