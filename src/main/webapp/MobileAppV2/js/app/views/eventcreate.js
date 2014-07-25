App.Views.EventCreate = Backbone.View.extend({
    el: '#app_view',
    events: {
        'submit': 'create'
    },
    bindings: {
        "#test1": {
            onSet: 'onSet'
        }
    },
    initialize: function () {
        Backbone.Validation.bind(this);
        this.model.bind('validated', function(isValid, model, errors) {

        });
    },
    template: _.template($("#template-eventcreate").html()),
    render: function(eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        this.$start = this.$(".startDateTime");
        this.$end = this.$(".endDateTime");

        this.$start.datetimepicker();
        this.$end.datetimepicker();

        return this;
    },
    create: function (e) {
        e.preventDefault();
        this.$type = this.$("input:radio[name='type']:checked");

        this.model.set('type', this.$type.val());
        this.model.set('start', this.$start.data("DateTimePicker").getDate());
        this.model.set('end', this.$end.data("DateTimePicker").getDate());

        var isValid = this.model.isValid(true);

        if (isValid)
        {
            var event = this.model.clone();
            app.model.events.add(event);
            event.save();
            Backbone.history.navigate('event/list', true);
        } else { alert('not valid'); }
    },
    onSet: function() { alert('onSet'); }
});

