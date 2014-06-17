App.Views.AppView = Backbone.View.extend({
	// Attach to existing elemnt
	el: "#app_view",

	template: _.template($("#template-appview").html()),

	render: function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	}
});