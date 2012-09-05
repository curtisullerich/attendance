App.MainView = Backbone.View.extend({
	// Attach to existing elemnt
	el: "#main_view",

	template: _.template($("#template-root").html())
});