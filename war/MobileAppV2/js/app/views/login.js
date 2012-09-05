App.Views.Login = Backbone.View.extend({
	// Attach to existing elemnt
	el: "#app_view",

	initialize:function () {
        this.model.bind("reset", this.render, this);
    },

	template: _.template($("#template-login").html()),

	render: function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	},

	events: {
        "submit form#login_form": "loginAction"
    },
    loginAction: function() {

    	var name = $("#loginName").val();
    	var password = $("#loginPassword").val();

    	if(this.login(name, password))
    	{
    		this.model.loggedin = true;
    	}

    	if(this.model.loggedin)
    	{

    	} else {

    	}

    	return false;
    },
    login: function(name, password) {

    	return false;
    }
});