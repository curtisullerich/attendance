var App = Backbone.Model.extend({
	localStorage: new Backbone.LocalStorage("App")
});

var DataUpload = Backbone.Model.extend({
	localStorage: new Backbone.LocalStorage("DataUpload"),
	url: "/MobileApp/data/upload"
});

var ClassList = Backbone.Model.extend({
	defaults: function() {
		return {
			updated: "Never"
		};
	},
	save: function() {

	},
	localStorage: new Backbone.LocalStorage("DataUpload"),
	url: "/MobileApp/data/classlist"
});

var TA = Backbone.Model.extend({
	initialize: function() {
		
	}
});

var TAs = Backbone.Collection.extend({
	model: TA,
	localStorage: new Backbone.LocalStorage("TAs")
});


var Student = Backbone.Model.extend({
	initialize: function() {

	}
});

var Students = Backbone.Collection.extend({
	model: Student,
	localStorage: new Backbone.LocalStorage("Students")
});