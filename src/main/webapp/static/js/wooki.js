/*
 * Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
if(!Wooki) var Wooki = {};

jQuery.extend(Wooki, {
	
});

/**
 * JQuery utils
 *
 */
jQuery.fn.outerHtml = function() {
	return jQuery(jQuery('<div></div>').html(this.clone())).html();
};

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
			Tapestry.loadScriptsInReply(reply, function(){});
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
	
	initFlashMsgBox : function(elt) {
		jQuery.notifyBar( {
			html : jQuery("#" + elt).html(),
			animationSpeed : "normal",
			cls : "wooki-flash"
		});
	},

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
	 * This method observe a link click event and reset the related form element.
	 * 
	 */
	resetFormOnClick : function(lnkId, formId) {
		$(lnkId).observe('click', function(event) {
			
			Event.stop(event);
			
			$(formId).reset();
	        
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
	openJQueryAjaxDialogOnClick : function(element, zoneId, dialogId, url) {
		
        element = $(element);

        $T(element).zoneId = zoneId;
        
        element.observe("click", function(event)
        {
            Event.stop(event);

            jQuery('#'+dialogId).dialog('open');

            var zoneObject = Tapestry.findZoneManager(element);

            if (!zoneObject) return;

            setTimeout(function() { zoneObject.updateFromURL(url); }, 250);
                        
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
	 * Open a link url in a new window.
	 *
	 */
	initOpenInWindow : function(param) {
		if(param == undefined) {
			return;
		}
		$(param.elt).observe('click', function(event) {
			Event.stop(event);
			window.open(param.url, param.name, param.options);
		});
	},
	
	/**
	 * Initialize Login Dialog Box
	 * 
	 */
	initLoginDialog : function(data) {
		link = jQuery("#signin-link");
		dialog = jQuery('#signin-box');

		x = link.position().left + link.width() * 2 - (parseInt(dialog.css('padding-right'))* 2) - dialog.width();
		y = link.position().top + 5 + link.height() + parseInt(link.css('padding-bottom'));
		
		link.toggle(
			function () {
				dialog.css({
					'top': y + 'px',
					'left': x + 'px',
					'display': 'block'
				});
				
				jQuery('#signin-username').focus();
			},
			function () {
				dialog.css('display', 'none');
			});
		
		jQuery("#content").bind("click mouseenter", dialog, function(event) {
			event.data.css('display', 'none');
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
		if(data !== undefined && data.params !== undefined) {
			
			// Configure wooki specifics
		    var containersItems = [
		                      {'name': 'P', 'title': 'Paragraph', 'css': 'wym_containers_p'},
		                      {'name': 'H3', 'title': 'Heading_3', 'css': 'wym_containers_h3'},
		                      {'name': 'H4', 'title': 'Heading_4', 'css': 'wym_containers_h4'},
		                      {'name': 'H5', 'title': 'Heading_5', 'css': 'wym_containers_h5'},
		                      {'name': 'H6', 'title': 'Heading_6', 'css': 'wym_containers_h6'},
		                      {'name': 'PRE', 'title': 'Preformatted', 'css': 'wym_containers_pre'},
		                      {'name': 'BLOCKQUOTE', 'title': 'Blockquote', 'css': 'wym_containers_blockquote'},
		                      {'name': 'TH', 'title': 'Table_Header', 'css': 'wym_containers_th'}];
			data.params.containersItems = containersItems;
			
			jQuery('#'+data.elt).wymeditor(data.params);
			jQuery.wymeditors(0).fullscreen();

		}
	},
	
	/**
	 * Init a link to launch an event if the resulting JSON contains true then
	 * the elt is removed on the client side.
	 * 
	 */
	initClickAndRemove : function(data) {
		$(data.elt).observe('click', function(e) {
			
			if(!window.confirm("Are you sure you want to delete this item ?")) {
				Event.stop(e);
				return;
			}

			jQuery.getJSON(data.url, function(result) {
				if(result.result) {
					jQuery('#'+data.toRemove).hide();
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
		jQuery("#content").append(bubbleDiv);
		
		// Iterate through commentable block to create corresponding comment elt
		jQuery('.commentable').each(function(i) {
		
			// Add element
			blockId = jQuery(this).attr('id');
			
			if(blockId != "") {
				comId = blockId.replace('b','c');
				
				// Add comment entry
				comment = jQuery("<a/>").attr({"id": comId, "class":"comment-accessor"});
	
				jQuery("#comments").append(comment);
				
				jQuery("#" + comId).append("<div class=\"no-comment\">&nbsp;</div>");
				
				Tapestry.Initializer.openJQueryAjaxDialogOnClick(comId, data.zoneId, data.dialogId, data.url.replace('blockId', blockId) );
				comment.css({
					'top': (jQuery(this).position().top - 5) + 'px',
					'left': (jQuery(this).position().left - 50)  + 'px',
					'height' : jQuery(this).height() + 'px'
				});
				
				comment.css('visibility','visible');
				
				jQuery(this).bind("mouseenter mouseleave", function(e){
					jQuery(".comment-accessor .no-comment").css('visibility','hidden');
				    jQuery('#' +jQuery(this).attr('id').replace('b','c') + ' .no-comment').css('visibility', 'visible');
				});
			}
			
		});
		
		// Set all the node that has comments
		jQuery.each(data, function(i, val) {
			comId = i.replace('b','c');
			jQuery("#" + comId + " div").attr("class", "commented").css('visibility', 'visible').append(document.createTextNode(val));
		});
		
		jQuery('#content').bind("mouseleave", function(e){
			jQuery(".comment-accessor .no-comment").css('visibility','hidden');
		});

		// On dialog close update all the bubbles
		jQuery('#'+data.dialogId).bind('dialogbeforeclose', function(e, ui) {
			jQuery.getJSON(data.updateUrl, function(result) {
				jQuery('.commentable').each(function(i) {
					blockId = jQuery(this).attr('id');
					comId = blockId.replace('b','c');
					nb = eval('result.' + blockId);
					if(nb != undefined) {
						jQuery("#" + comId + " div").attr("class", "commented").css('visibility', 'visible').text(nb);
					}else {
						jQuery("#" + comId + " div").attr("class", "no-comment").text("");
					}
				});
			});
		});
		
		// Remove content on close
		jQuery('#'+data.dialogId).bind('dialogclose', function(e, ui) {
			jQuery('#'+data.zoneId).html("<div style=\"padding:10px;\"><div class=\"t-autoloader-icon\"/></div>");
		});
        	
	},
	
	/**
	 * Init the block reminder in the comment dialog
	 */
	initBlockReminder: function(domId) {
		jQuery('#reminder-'+domId).append(jQuery('#'+domId).outerHtml());
	},
	
	/**
	 * Initialize close links in dialog. 
	 *
	 */
	initCloseLink: function(dialogId) {
        $A($(dialogId).getElementsByClassName("close-dialog")).each(function(closeLnk) {
        	closeLnk.observe("click", function() {
        		jQuery("#"+dialogId).dialog('close');
        	});
        });
	},
	
	/**
	 * 
	 */
	initConfirm: function(params) {
		if(params !== undefined && params.lnkId !== undefined) {
			$(params.lnkId).observe('click', function(e) {
				message = "Are you sure you want to delete this item ?";
				if(params.message !== undefined) {
					message = params.message;
				}
				if(!window.confirm(message)) {
					Event.stop(e);
					return;
				}
			}.bind(params));
		}
	},

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
				return false;
			});
		}
	},
	
	/**
	 * Init a link that will update a zone with a 
	 *
	 */
	initMoreLink: function(data) {
		if(data != undefined) {
			jQuery("#"+data.elt).bind("click", data, function(event) {
				jQuery("#"+data.loader).show();
				var successHandler = function(reply) {
					if (!reply.empty) {
						if (data.position == "BOTTOM") {
							jQuery("#" + event.data.zone).append(reply.content);
						} else {
							jQuery("#" + event.data.zone).prepend(reply.content);
						}
						jQuery("#" + data.elt).attr("href", reply.href);
						if (!reply.hasMore) {
							jQuery("#" + data.elt).closest(".more-link").remove();
						}
						jQuery("#" + data.loader).hide();
					} else {
						jQuery("#" + data.elt).closest(".more-link").remove();
					}
				};
				setTimeout(jQuery.getJSON(jQuery("#" + data.elt).attr("href"), successHandler), 250);
				return false;
			});
		}
	}

});

// Do not not desactivate Zone after a link is clicked.
Event.observe(window, "beforeunload", function()
{
    Tapestry.windowUnloaded = false;
});