if(!Wooki) var Wooki = {};

jQuery.extend(Wooki, {
	bubbles :  {
		init : function() {
			jQuery('.commentable').each(function(i) {
				
				comment = jQuery('#' +jQuery(this).attr('id').replace('b','c'));
				comment.css({
					'top': (jQuery(this).offset().top + 10) + 'px',
					'left': (jQuery(this).offset().left - 50)  + 'px',
					'height' : jQuery(this).height() + 'px',
				});
				
				comment.css('visibility','visible');
				
				jQuery(this).bind("mouseenter mouseleave", function(e){
					jQuery(".comment-accessor .no-comment").css('visibility','hidden');
							    
				    jQuery('#' +jQuery(this).attr('id').replace('b','c') + ' .no-comment').css('visibility', 'visible');
				});
			});
			
			jQuery('#book').bind("mouseleave", function(e){
				jQuery(".comment-accessor .no-comment").css('visibility','hidden');
			});
		}
	}
});


/**
 * Add initialization method to Tapestry.Initializer object
 */

jQuery.extend(Tapestry.Initializer,{
	/**
	 * Convert a form or link into a trigger of an Ajax update that updates the
	 * indicated Zone.
	 * 
	 * @param element
	 *            id or instance of <form> or <a> element
	 * @param zoneId
	 *            id of the element to update when link clicked or form
	 *            submitted
	 * @param url
	 *            absolute component event request URL
	 */
	openJQueryAjaxDialogOnClick : function(element, zoneId, dialogId, url)
    {
        element = $(element);

        $T(element).zoneId = zoneId;
        
        element.observe("click", function(event)
        {
            Event.stop(event);

            var zoneObject = Tapestry.findZoneManager(element);

            if (!zoneObject) return;

            jQuery('#'+dialogId).dialog('open');
            zoneObject.updateFromURL(url);
        });
    },
	
	/**
	 * Initialize jquery dialog popup on click of a elt.
	 */
	openJQueryDialogOnClick : function(triggerId, dialogId) { 
		jQuery('#'+triggerId).click(function() {
			jQuery('#'+dialogId).dialog('open');
		});
	},
	
	/**
	 * Initialize Login Dialog Box
	 * 
	 */
	initLoginDialog : function(data) {
		link = jQuery("#login-link");
		dialog = jQuery('#login-dialog-form')

		x = link.position().left + link.width() + (parseInt(dialog.css('padding-right'))* 2) - dialog.width();
		y = link.position().top + link.height() + parseInt(link.css('padding-bottom'));
		
		link.toggle(
			function () {
				link.addClass('login-link-active');
				dialog.css({
					'top': y + 'px',
					'left': x + 'px',
					'display': 'block'
				});
			},
			function () {
				link.removeClass('login-link-active');
				dialog.css('display', 'none');
			});
			       
			
	},

	/**
	 * Build a JQuery Dialog box.
	 * 
	 */
	initJQueryDialog : function(data) {
		jQuery('#'+data.elt).dialog(data.params);
	},

	/**
	 * Transform a textarea object into a wymeditor instance.
	 * 
	 */
	initWymEdit : function(data) {
		jQuery('#'+data.elt).wymeditor(data.params);
	},	
});