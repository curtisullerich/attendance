App.Views.Event = Backbone.View.extend({
    initialize: function () {
        this.render();
    },
    template: _.template($("#template-event").html()),
    render: function(eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    }
});

