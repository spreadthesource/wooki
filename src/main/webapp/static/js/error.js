jQuery.extend(Tapestry.Initializer, {

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