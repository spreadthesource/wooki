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
	 * Initialize jquery dialog popup on click of a elt.
	 */
	openJQueryDialogOnClick : function(triggerId, dialogId) { 
		jQuery('#'+triggerId).click(function() {
			jQuery('#'+dialogId).dialog('open');
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