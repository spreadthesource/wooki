//Extend WYMeditor
WYMeditor.editor.prototype.uploadImageDialog = function() {
	var wym = this;

	// construct the button's html
	var html = "<li class='wym_upload_image'>"
			+ "<a name='Upload Image' href='#'" + " style='background:"
			+ " url(" + wym._options.basePath
			+ "plugins/upload-image-dialog/picture_link.png) no-repeat 2px 4px;'>" + "Upload Image"
			+ "</a></li>";

	// add the button to the tools box
	jQuery(wym._box).find(
			wym._options.toolsSelector + wym._options.toolsListSelector)
			.append(html);

	var button = jQuery(wym._box).find('li.wym_upload_image a');
	
	// Create upload dialog upon added button
	new AjaxUpload(button, {
		action: wym._options.uploadAction,
		name: 'attachment',
		data: wym._options.uploadDatas,
		onSubmit : function(file, ext) {
							
			 if (! (ext && /^(jpg|png|jpeg|gif)$/i.test(ext))){
                 // extension is not allowed
                 alert('Error: invalid file extension');
                 // cancel upload
                 return false;
			 }
			
		},
		onComplete: function(file, response){

			// window.clearInterval(interval);
						
			// enable upload button
			// this.enable();
			
			// Add uploaded file link
			var sStamp = wym.uniqueStamp();
			
	        wym._exec(WYMeditor.INSERT_IMAGE, sStamp);

			jQuery("img[src$=" + sStamp + "]", wym._doc.body)
            	.attr(WYMeditor.SRC, response)
            	.attr(WYMeditor.TITLE, file)
            	.attr(WYMeditor.ALT, file);
			
		}
	});
	
};
