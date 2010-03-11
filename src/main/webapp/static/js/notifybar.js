jQuery.extend(Tapestry.Initializer, {

	/**
	 * Display an information message from an HTML element.
	 *
	 */
	initFlashMsgBox : function(elt) {
		jQuery.notifyBar( {
			html : jQuery("#" + elt).html(),
			animationSpeed : "normal",
			cls : "wooki-flash"
		});
	},

	/**
	 * Display an information message from an text element.
	 *
	 */
	initFlashMessage : function(response, listClass, notifyClass) {
		if(response.messages != undefined) {
			var message = "<div class=\"" + listClass +" shadowed\"><ul class=\"" + listClass +" wrapper\">";
			jQuery.each(response.messages, function(){
				message += "<li>" + this + "</li>";
			});
			message += "</ul></div>"
			jQuery.notifyBar( {
				html : message,
				animationSpeed : "normal",
				cls : notifyClass
			});
		}
	},
	
	/**
	 * Init the error box movement.
	 * 
	 */
	initErrorBox : function(elt) {
		jQuery.notifyBar( {
			html : jQuery("#" + elt).html(),
			animationSpeed : "normal",
			cls : "wooki-error"
		});
	},

	/**
	 * Used by Append mixin to display errors when the form submission process
	 * has encountered errors.
	 */
	initErrorMessage : function(message) {
		if(message != undefined) {
			jQuery.notifyBar( {
				html : message.html,
				animationSpeed : "normal",
				cls : "wooki-error"
			});
		}
	}

});