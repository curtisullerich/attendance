App.Views.EventListing = Backbone.View.extend({
    el: '#app_view',
    initialize: function () {
        this.eventsView = new App.Views.Events({model: this.model});
    },
    template: _.template($("#template-eventlist").html()),
    render: function(eventName) {
        $(this.el).html(this.template(this.model.toJSON()));

        this.eventsView.$el = this.$('.events');
        this.eventsView.render();

        return this;
    }
});

