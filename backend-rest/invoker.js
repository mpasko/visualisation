var invokeAQ21 = function(query,callback,onerror) {
	var ready = JSON.stringify(query);
	var resource = 'http://localhost/jersey/aq21/postIt';
	var ourType = 'application/json';
	jQuery.ajax({
		type: 'POST',
		contentType: ourType,
		dataType: 'json',
		url: resource,
		data: ready,
		cache: false,
		success: function(data) {
			callback(data);
		},
		processData: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			onerror();
		}
	});
};

var convertFromCSV = function(query,callback,onerror) {
	var ready = JSON.stringify(query);
	var resource = 'http://localhost/jersey/aq21/fromCSV';
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
			callback(data["events"]);
		},
		processData: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			onerror();
		}
	});
};

var convertFromAQ21 = function(query,callback,onerror) {
	var ready = JSON.stringify(query);
	var resource = 'http://localhost/jersey/aq21/fromAQ21';
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
			callback(data);
		},
		processData: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			onerror();
		}
	});
};

var saveToDB = function(query,onerror) {
		var ready = JSON.stringify(query);
	var resource = 'http://localhost/jersey/aq21/saveExperiment';
	var ourType = 'application/json';
	jQuery.ajax({
		type: 'POST',
		contentType: ourType,
		crossDomain: true,
		url: resource,
		data: ready,
		headers: {'Access-Control-Allow-Origin:': '*'},
		cache: false,
		processData: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			onerror();
		}
	});
}

var saveResultToDB = function(query,onerror) {
		var ready = JSON.stringify(query);
	var resource = 'http://localhost/jersey/aq21/saveResult';
	var ourType = 'application/json';
	jQuery.ajax({
		type: 'POST',
		contentType: ourType,
		crossDomain: true,
		url: resource,
		data: ready,
		headers: {'Access-Control-Allow-Origin:': '*'},
		cache: false,
		processData: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			onerror();
		}
	});
}

var browseDB = function(callback,onerror) {
	ready = "";
	var resource = 'http://localhost/jersey/aq21/browse';
	var ourType = 'application/json';
	jQuery.ajax({
		type: 'GET',
		contentType: ourType,
		dataType: 'json',
		url: resource,
		data: ready,
		cache: false,
		success: function(data) {
			callback(data);
		},
		processData: false,
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			onerror();
		}
	});
};

var stopService = function() {
	var resource = 'http://localhost/jersey/aq21/stop';
	jQuery.ajax({
		type: 'GET',
		dataType: 'text/plain',
		crossDomain: true,
		url: resource,
		cache: false,
		success: function(data) {
			var result = data;
		},
		processData: false,
	});
};
