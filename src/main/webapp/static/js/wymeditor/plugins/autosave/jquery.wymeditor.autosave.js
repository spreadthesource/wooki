//Extend WYMeditor
WYMeditor.editor.prototype.autosave = function() {

	var wym = this;
	
	var interval = wym._options.autosaveInterval;
	var once =  wym._options.autosaveInterval == -1;
	
	if (once) {
		interval = 10000;
	}
	
	jQuery.timer(interval, function(timer){
				
		// Update and download
		wym.update();
		var form = jQuery("#" + wym._options.formId);
		form[0].sendAjaxRequest(form.attr("action"), {
			onSuccess: function(transport) {
				jQuery(".form-submit").append("<span class=\"autosave\">(" + transport.responseJSON.message + ")</span>");
			},
			onFailure: function() {
				jQuery(".form-submit").append("<span class=\"autosave\">(Last auto saved failed)</span>");
			}
		});
		
		if (once) {
			timer.stop();
		}

	});
	
};
