App.Views.Main = Backbone.View.extend({
    el: "#app_view",

    initialize:function () {
        this.model.bind("reset", this.render, this);

        //TODO App..fetch();
    },

    template: _.template($("#template-mainview").html()),

	render: function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	},

    events: {
        "click #upload": "upload",
        "click #update": "update"
    },
    upload: function() {
    	alert("upload");
    	return false;
    },
    update: function() {
    	alert("update");
    	return false;
    }

});

