WYMeditor.SKINS['wooki'] = {

    init: function(wym) {
	

        //render following sections as panels
        jQuery(wym._box).find(wym._options.classesSelector)
          .addClass("wym_panel");

        //render following sections as buttons
        jQuery(wym._box).find(wym._options.toolsSelector)
          .addClass("wym_buttons");

        //render following sections as dropdown menus
        jQuery(wym._box).find(wym._options.classesSelector + ', ' + wym._options.containersSelector)
          .addClass("wym_dropdown");
        
        //make hover work under IE < 7
        jQuery(wym._box).find(".wym_section").hover(function(){
          jQuery(this).addClass("hover");
        },function(){
          jQuery(this).removeClass("hover");
        });
    }
};
