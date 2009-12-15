if(!Wooki) var Wooki = {};

jQuery.extend(Wooki, {
	
});

Tapestry.ElementEffect.none = function(element) {
	// Do nothing
};

/**
 * Submit the current for via Ajax and insert result into an element in
 * the page.
 *
 */
Tapestry.Append = Class.create({
	
	/**
	 * Initialize the form to submit via Ajax and add the corresponding
	 * insertion mechanism in response.
	 *
	 */
	initialize: function(url, element, to, position) {
	
		this.element = $(element);
		
		this.to = $(to);
		
		this.position = position;
		
		this.element.insert({'top': "<div class='append-errors'/>"});
		
		// Turn normal form submission off.
	
	    this.element.getFormEventManager().preventSubmission = true;
	
	    // After the form is validated and prepared, this code will
	    // process the form submission via an Ajax call.  The original submit event
	    // will have been cancelled.
	
	    this.element.observe(Tapestry.FORM_PROCESS_SUBMIT_EVENT, function() {
	        var successHandler = function(transport) {
	            this.processReply(transport.responseJSON, this.position);
	        }.bind(this);
	
	        this.element.sendAjaxRequest(url, {
	            onSuccess : successHandler
	        });
	    }.bind(this));
	
	},
	
	/** 
	 * Execute script and load result content in the target element.
	 */
	processReply : function(reply, position) {
		if (reply.errors != undefined) {
			var errorDiv = this.element.down('.append-errors');
			errorDiv.update(reply.errors);
			errorDiv.show();
			errorDiv.fade({duration : 5.0});
		} else {
			Tapestry.loadScriptsInReply(reply, function() {
				if(position == 'bottom') {
					Element.insert(this.to, {'bottom' : reply.content});
				}else{
					Element.insert(this.to, {'top' : reply.content});
				}
	        }.bind(this));
		}
	}
	
});

/**
 * Add initialization method to Tapestry.Initializer object
 */

jQuery.extend(Tapestry.Initializer,{

	/**
	 * This method overrides the update method to insert new content instead of
	 * replacing current content.
	 * 
	 */
	submitFormOnChange : function(inputId, formId) {
		$(inputId).observe('change', function(event) {
			
			Event.stop(event);
			
			var onsubmit = $(formId).onsubmit;

	        if (onsubmit == undefined || onsubmit.call(window.document, event)) {
	        	$(formId).submit();
	        }
	        
	        return false;
			
		});
	},
	
	/**
	 * This method overrides the update method to insert new content instead of
	 * replacing current content.
	 * 
	 */
	appendToZone : function(url, element, to, position) {
		new Tapestry.Append(url, element, to, position);
	},
	
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

		x = link.position().left + link.width() - (parseInt(dialog.css('padding-right'))* 2) - dialog.width();
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
	 * Init a link to launch an event if the resulting JSON contains true then
	 * the elt is removed on the client side.
	 * 
	 */
	initClickAndRemove : function(data) {
		$(data.elt).observe('click', function(e) {
			jQuery.getJSON(data.url, function(result) {
				if(result.result) {
					jQuery('#'+data.toRemove).remove();
				}
			});
			e.stop();
		});
	},

	/**
	 * Init bubble when a chapter is displayed on screen
	 * 
	 * data parameter contains the list of blocks that has comments and their
	 * number.
	 */
	initBubbles : function(data) {

		// Append comments div
		var bubbleDiv = jQuery("<div/>").attr("id", "comments");
		jQuery(".entry-content").append(bubbleDiv);
		
		// Iterate through commentable block to create corresponding comment elt
		jQuery('.commentable').each(function(i) {
		
			// Add element
			blockId = jQuery(this).attr('id');
			comId = blockId.replace('b','c');
			
			// Add comment entry
			comment = jQuery("<a/>").attr({"id": comId, "class":"comment-accessor"});

			jQuery("#comments").append(comment);
			
			jQuery("#" + comId).append("<div class=\"no-comment\">&nbsp;</div>");
			
			Tapestry.Initializer.openJQueryAjaxDialogOnClick(comId, data.zoneId, data.dialogId, data.url.replace('blockId', blockId) );
			
			comment.css({
				'top': (jQuery(this).position().top + 10) + 'px',
				'left': (jQuery(this).position().left - 50)  + 'px',
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

		// On dialog close update all the bubbles
		jQuery('#'+data.dialogId).bind('dialogclose', function(e, ui) {
			jQuery.getJSON(data.updateUrl, function(result) {
				jQuery('.commentable').each(function(i) {
					blockId = jQuery(this).attr('id');
					comId = blockId.replace('b','c');
					nb = eval('result.' + blockId);
					if(nb != undefined) {
						jQuery("#" + comId + " div").attr("class", "commented").css('visibility', 'visible').text(nb);
					}else {
						jQuery("#" + comId + " div").attr("class", "no-comment");
					}
				});
			});
		});
	
	},
	
	/**
	 * Init the block reminder in the comment dialog
	 */
	initBlockReminder: function(domId) {
		jQuery('#reminder-'+domId).append(jQuery('#'+domId).html());
	}
	
});