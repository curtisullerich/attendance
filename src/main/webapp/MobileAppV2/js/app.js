/**
 * App() object
 *  
 */
var App = {
	Views: {}
};

App.Router = Backbone.Router.extend({

	routes:{
		"":"main",
		"checkinout":"check_in_out",
		"events":"events",
		"event/:id":"event"
	},

	main: function()
	{
		App.mainview.render();
	},
	check_in_out: function()
	{
		App.checkinout.render();
	},
	events: function()
	{
		App.mainview.render();
	},
	event: function(id)
	{
		App.mainview.render();
	}
});

