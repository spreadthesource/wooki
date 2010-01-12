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
	}

});