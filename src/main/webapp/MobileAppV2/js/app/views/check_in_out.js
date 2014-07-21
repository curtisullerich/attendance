App.Views.CheckInOut = Backbone.View.extend({
	el: "#app_view",

	template: _.template($("#template-checkinoutview").html()),

	render: function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	}
});

