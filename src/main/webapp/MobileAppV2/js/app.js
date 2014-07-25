var app = {};

var App = App || {Views:{}};

App.Router = Backbone.Router.extend({

	routes:{
		"":"main",
		"checkinout":"check_in_out",
		"event/list":"event_list",
        "event/new":"event_create",
		"event/:id":"event"
	},

	main: function()
	{
		app.mainview.render();
	},
	check_in_out: function()
	{
		app.checkinout.render();
	},
	event_list: function()
	{
		app.eventlisting.render();
	},
	event: function(id)
	{
		app.event.render();
	},
    event_create: function()
    {
        app.event_create.render();
    }
});

$(function () {
    var model = {};
    app.model = model;
    model.app = new App.Model.App;
    model.events = new App.Model.Events;
    model.students = new App.Model.Students;
    model.checkinout = new App.Model.CheckInOut({students: model.students});
    app.router = new App.Router;

    app.eventlisting = new App.Views.EventListing({model: model.events});
    app.mainview = new App.Views.Main({model: model.app});
    app.checkinout = new App.Views.CheckInOut({model: model.checkinout});
    app.event_create = new App.Views.EventCreate({model: new App.Model.Event});

    Backbone.history.start();
});

