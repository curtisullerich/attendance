var App = Backbone.RelationalModel.extend({
	defaults: function() {
		return {
			updated: "Never"
		};
	},
	relations: [{
		type: Backbone.HasMany,
		key: 'students',
		relatedModel: 'Student',
		collectionType: 'Students'
	}]
	localStorage: new Backbone.LocalStorage("MobileApp")
});

var Absence = Backbone.Model.extend({
	initialize: function() {

	}
});

var Event = Backbone.Model.extend({
	initialize: function() {

	}
});

var DataUpload = Backbone.Model.extend({
	localStorage: new Backbone.LocalStorage("DataUpload"),
	url: "/MobileApp/data/upload"
});


var Student = Backbone.Model.extend({
	initialize: function() {

	}
});

var Students = Backbone.Collection.extend({
	model: Student,
	localStorage: new Backbone.LocalStorage("Students"),
	url: "/MobileApp/data/classlist"
});