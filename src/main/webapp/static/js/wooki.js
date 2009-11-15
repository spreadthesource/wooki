if(!Wooki) var Wooki = {};

jQuery.extend(Wooki, {
	bubbles :  {
		init : function() {
			jQuery('.commentable').each(function(i) {
			jQuery(this).bind("mouseenter mouseleave", function(e){
				jQuery(".comment-accessor .no-comment").css('visibility','hidden');
						
				comment = jQuery('#' +jQuery(this).attr('id').replace('b','c'));
				comment.css({
					'top': (jQuery(this).offset().top + 10) + 'px',
					'left': (jQuery(this).offset().left - 50)  + 'px',
					'height' : jQuery(this).height() + 'px',
				});
			    comment.css('visibility','visible');
			    
			    jQuery('#' +jQuery(this).attr('id').replace('b','c') + ' .no-comment').css('visibility', 'visible');
			 });
			});
			
			jQuery('#book').bind("mouseleave", function(e){
				jQuery(".comment-accessor .no-comment").css('visibility','hidden');
			});
		}
	}
});