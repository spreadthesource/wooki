if(!Wooki) var Wooki = {};

jQuery.extend(Wooki, {
	
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
	
	/**
	 * Init bubble when a chapter is displayed on screen
	 * 
	 * data parameter contains the list of blocks that has comments and their number.
	 */
	initBubbles : function(data) {

		// Append comments div
		var bubbleDiv = jQuery("<div/>").attr("id", "comments");
		jQuery(".entry-content").append(bubbleDiv);
		
		// Iterate through commentable block to create corresponding comment elt
		jQuery('.commentable').each(function(i) {
		
			// Add element
			blockId = jQuery(this).attr('id');
			comId = jQuery(this).attr('id').replace('b','c');
			
			// Add comment entry
			comment = jQuery("<a/>").attr("id", comId).attr("class", "comment-accessor");
			jQuery("#comments").append(comment);

			jQuery("#comments").append(comment);
			
			jQuery("#" + comId).append("<div class=\"no-comment\">&nbsp;</div>");
			
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
		
		// Set all the node that has comments
		jQuery.each(data, function(i, val) {
			comId = i.replace('b','c');
			jQuery("#" + comId + " div").attr("class", "commented").css('visibility', 'visible').append(document.createTextNode(val));
		});
		
		
		jQuery('#book').bind("mouseleave", function(e){
			jQuery(".comment-accessor .no-comment").css('visibility','hidden');
		});
	}
});