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
		this.checkLogin() && App.mainview.render();
	},
	check_in_out: function()
	{
		this.checkLogin() && App.checkinout.render();
	},
	events: function()
	{
		this.checkLogin() && App.mainview.render();
	},
	event: function(id)
	{
		this.checkLogin() && App.mainview.render();
	},
	checkLogin: function()
	{
		if(!App.model.get('loggedin'))
		{
			App.loginview.render();
		}

		return App.model.loggedin;
	}
});