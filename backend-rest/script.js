
var query;

function updateQuery(result){
	query = result;
	var text = JSON.stringify(result).replace(/\n/g, "<br>");
	jQuery('#Request').empty();
	jQuery('#Request').append(text);
}

function readSingleFile(evt) {
	//Retrieve the first (and only!) File from the FileList object
    var f = evt.target.files[0]; 

	if (f) {
		var r = new FileReader();
		r.onload = function(e) { 
			var csv = e.target.result;
			jQuery('#Response').empty();
			convertFromAQ21(csv, function(result){
				//On success
				updateQuery(result);
			}, function(){
				//On error
				jQuery('#Request').append("Request failed");
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
	updateQuery({
"id" : 0,
"runsGroup" : {
"globalLearningParameters" : [ ],
"runs" : [ {
"name" : "Run_c1",
"runSpecificParameters" : [ {
"name" : "Mode",
"value" : "TF",
"id" : 0,
"parent" : "Run_c1"
}, {
"name" : "Consequent",
"value" : "[class=c1]",
"id" : 1,
"parent" : "Run_c1"
}, {
"name" : "Ambiguity",
"value" : "IncludeInPos",
"id" : 2,
"parent" : "Run_c1"
}, {
"name" : "Trim",
"value" : "Optimal",
"id" : 3,
"parent" : "Run_c1"
}, {
"name" : "Compute_alternative_covers",
"value" : "True",
"id" : 4,
"parent" : "Run_c1"
}, {
"name" : "Maxstar",
"value" : "1",
"id" : 5,
"parent" : "Run_c1"
}, {
"name" : "Maxrule",
"value" : "10",
"id" : 6,
"parent" : "Run_c1"
} ]
}, {
"name" : "Run_c2",
"runSpecificParameters" : [ {
"name" : "Mode",
"value" : "TF",
"id" : 7,
"parent" : "Run_c2"
}, {
"name" : "Consequent",
"value" : "[class=c2]",
"id" : 8,
"parent" : "Run_c2"
}, {
"name" : "Ambiguity",
"value" : "IncludeInPos",
"id" : 9,
"parent" : "Run_c2"
}, {
"name" : "Trim",
"value" : "Optimal",
"id" : 10,
"parent" : "Run_c2"
}, {
"name" : "Compute_alternative_covers",
"value" : "False",
"id" : 11,
"parent" : "Run_c2"
}, {
"name" : "Maxstar",
"value" : "1",
"id" : 12,
"parent" : "Run_c2"
}, {
"name" : "Maxrule",
"value" : "1",
"id" : 13,
"parent" : "Run_c2"
} ]
}, {
"name" : "Run_All_in_PD",
"runSpecificParameters" : [ {
"name" : "Mode",
"value" : "PD",
"id" : 14,
"parent" : "Run_All_in_PD"
}, {
"name" : "Consequent",
"value" : "[class=*]",
"id" : 15,
"parent" : "Run_All_in_PD"
}, {
"name" : "Ambiguity",
"value" : "IncludeInPos",
"id" : 16,
"parent" : "Run_All_in_PD"
}, {
"name" : "Trim",
"value" : "Optimal",
"id" : 17,
"parent" : "Run_All_in_PD"
}, {
"name" : "Compute_alternative_covers",
"value" : "False",
"id" : 18,
"parent" : "Run_All_in_PD"
}, {
"name" : "Maxstar",
"value" : "1",
"id" : 19,
"parent" : "Run_All_in_PD"
}, {
"name" : "Maxrule",
"value" : "1",
"id" : 20,
"parent" : "Run_All_in_PD"
} ]
}, {
"name" : "Run_Multi-head",
"runSpecificParameters" : [ {
"name" : "Mode",
"value" : "PD",
"id" : 21,
"parent" : "Run_Multi-head"
}, {
"name" : "Consequent",
"value" : "[class=c1][length<=40]",
"id" : 22,
"parent" : "Run_Multi-head"
}, {
"name" : "Ambiguity",
"value" : "IncludeInPos",
"id" : 23,
"parent" : "Run_Multi-head"
}, {
"name" : "Trim",
"value" : "Optimal",
"id" : 24,
"parent" : "Run_Multi-head"
}, {
"name" : "Compute_alternative_covers",
"value" : "False",
"id" : 25,
"parent" : "Run_Multi-head"
}, {
"name" : "Maxstar",
"value" : "1",
"id" : 26,
"parent" : "Run_Multi-head"
}, {
"name" : "Maxrule",
"value" : "1",
"id" : 27,
"parent" : "Run_Multi-head"
} ]
} ],
"runsNames" : [ "Run_c1", "Run_c2", "Run_All_in_PD", "Run_Multi-head" ]
},
"testsGroup" : {
"globalLearningParameters" : [ ],
"runs" : [ ],
"runsNames" : [ ]
},
"domains" : [ {
"id" : 0,
"name" : "color",
"domain" : "nominal",
"parameters" : "{red, green, blue}"
} ],
"events" : [ {
"id" : "0",
"background" : "red",
"length" : "34.6",
"class" : "c1",
"number" : "1"
}, {
"id" : "1",
"background" : "green",
"length" : "2.45",
"class" : "c2",
"number" : "0"
}, {
"id" : "2",
"background" : "red",
"length" : "33.0",
"class" : "c1",
"number" : "1"
}, {
"id" : "3",
"background" : "blue",
"length" : "33.5",
"class" : "c2",
"number" : "0"
} ],
"outputHypotheses" : null,
"attributes" : [ {
"id" : 0,
"name" : "background",
"domain" : "color",
"parameters" : ""
}, {
"id" : 1,
"name" : "number",
"domain" : "linear",
"parameters" : "{ 0, 1, 2 }"
}, {
"id" : 2,
"name" : "length",
"domain" : "continuous",
"parameters" : "0, 200"
}, {
"id" : 3,
"name" : "class",
"domain" : "nominal",
"parameters" : "{c1, c2}"
} ],
"testingEvents" : [ ]
});
	
	jQuery('#Button').click(function(){
		jQuery('#Response').empty();
		invokeAQ21(query, function(result){
			//On success
			var text = JSON.stringify(result).replace(/\n/g, "<br>");
			jQuery('#Response').append(text);
		}, function(){
			//On error
			jQuery('#Response').append("Request failed");
		});
		return false;
	});
	
	document.getElementById('fileinput')
	.addEventListener('change', readSingleFile, false);
	
	jQuery(window).bind('beforeunload', function(){
		stopService();
	});

	jQuery(window).bind('onunload', function(){
		stopService();
	});
	
//	window.onbeforeunload = function (e){
		
//		return true;
//	}
});