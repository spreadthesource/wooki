if(!Wooki) var Wooki = {};

Wooki.Core = {
	Events : {
		SHOW: "show",
		HIDE: "hide"
	}
};

jQuery.extend(Tapestry.Initializer, {

	/**
	 * Implement a simple show hide effect for chapter addition.
	 *
	 */
	initShowHideEffect: function(data) {
		if(data != undefined) {
			jQuery("#"+data.showLnkId).bind("click", data, function(event) {
				if(jQuery("#"+event.data.showLnkId).disabled) {
					return false;
				}
				jQuery("#"+event.data.showLnkId).disabled = true;
				jQuery("#"+event.data.toShow).slideDown(event.data.duration);
				jQuery("#"+event.data.toShow).trigger(Wooki.Core.Events.SHOW);
				return false;
			});
			jQuery("#"+data.hideLnkId).bind("click", data, function(event) {
				if(event.data.formClass !== undefined) {
					jQuery("."+event.data.formClass).each(
						function (i, form) { 
							form.reset(); 
						}
					);
				}
				jQuery("#"+event.data.showLnkId).disabled = false;
				jQuery("#"+event.data.toShow).slideUp(data.duration);
				jQuery("#"+event.data.toShow).trigger(Wooki.Core.Events.HIDE);
				return false;
			});
		}
	}

});