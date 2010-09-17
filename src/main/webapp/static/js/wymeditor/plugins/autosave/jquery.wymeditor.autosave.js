//Extend WYMeditor
WYMeditor.editor.prototype.autosave = function() {

	var wym = this;

	var interval = wym._options.autosaveInterval;
	var once = wym._options.autosaveInterval == -1;
	var post_action = false;

	if (once) {
		interval = 10000;
	}

	jQuery(".wymupdate").each(function(i) {
		jQuery(this).bind('click', function() {
			post_action = true;
		});
	});

	jQuery(window)
			.bind(
					'beforeunload',
					function() {
						if (!post_action) {
							if (confirm("Doing this you may lose your current work, Save draft before leaving ?")) {
								wym.update();
								var form = jQuery("#" + wym._options.formId);
								form[0].setSubmittingElement("update");
								form[0].performSubmit();
							}
						}
					});

	jQuery
			.timer(
					interval,
					function(timer) {

						// Update and download
						wym.update();
						var form = jQuery("#" + wym._options.formId);
						form[0]
								.sendAjaxRequest(
										form.attr("action"),
										{
											onSuccess : function(transport) {
												if (jQuery("span.autosave") != undefined) {
													jQuery("span.autosave")
															.remove();
												}
												jQuery(
														"."
																+ wym._options.autosaveStatus)
														.append(
																"<span class=\"autosave\">("
																		+ transport.responseJSON.message
																		+ ")</span>");
											},
											onFailure : function() {
												if (jQuery("span.autosave") != undefined) {
													jQuery("span.autosave")
															.remove();
												}
												jQuery(
														"."
																+ wym._options.autosaveStatus)
														.append(
																"<span class=\"autosave\">(Last auto saved failed)</span>");
											}
										});

						if (once) {
							timer.stop();
						}

					});

};
