App.Views.Events = Backbone.View.extend({
    initialize: function () {
	    this.listenTo(this.model, 'reset', this.render);
        this.model.fetch({reset: true});
    },
    render: function(eventName) {
        this.$el.empty();
        var self = this;
        //$(this.el).html(this.template(this.model.toJSON()));

        this.model.each(function(item) {
            var view = new App.Views.Event({model: item});
            self.$el.append(view.el);
        });

        return this;
    }
});

