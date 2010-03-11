//Extend WYMeditor
WYMeditor.editor.prototype.autosave = function() {

	var wym = this;

	var html = "<li class='wym_autosave' style='display: none'>"
		+ "<p>Saving <img src='"+ wym._options.ajaxLoader +"' />"
		+ "</p></li>";

	//add the button to the tools box
	jQuery(wym._box)
	    .find(wym._options.toolsSelector + wym._options.toolsListSelector)
	    .append(html);
	
	jQuery.timer(5000, function(timer){
		
		jQuery(".wym_autosave").toggle();
		
		// Update and download
		wym.update();
		var form = jQuery("#" + wym._options.formId);
		form[0].sendAjaxRequest(form.attr("action"), {
			successHandler: function() {
				jQuery(".wym_autosave").toggle();
			},
			failureHandler: function() {
				jQuery(".wym_autosave").toggle();
			}
		});

	});
	
};
