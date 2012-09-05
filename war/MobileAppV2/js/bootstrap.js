$(function () {
	App.model = new App.Model.App();
	App.router = new App.Router();

	App.mainview = new App.Views.Main({model:App.model});
	App.checkinout = new App.Views.CheckInOut({model:App.model});
	App.loginview = new App.Views.Login({model:App.model});
	//App.checkinout = new App.Views.CheckInOut({model:App.model});

	// Kick it off
	Backbone.history.start();
});