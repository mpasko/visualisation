var invokeAQ21 = function(query,callback,onerror) {
	var ready = JSON.stringify(query);
	var resource = 'http://localhost:9998/jersey/aq21/postIt';
	var ourType = 'application/json';
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
			var result = JSON.stringify(data["outputHypotheses"]);
			callback(result);
		},
		processData: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			onerror();
		}
	});
};

var convertFromCSV = function(query,callback,onerror) {
	var ready = JSON.stringify(query);
	var resource = 'http://localhost:9998/jersey/aq21/fromCSV';
	var ourType = 'text/plain';
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
			var result = JSON.stringify(data["events"]);
			callback(result);
		},
		processData: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			onerror();
		}
	});
};