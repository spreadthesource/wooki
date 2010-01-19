package com.wooki.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;


/**
 * Use to display IE CSS Comment containing scripts.
 *
 * @author ccordenier
 *
 */
public class IExploreCssComment {

	@Parameter
	private Asset script;
	
	@SetupRender
	private void displayComment(MarkupWriter writer){
		writer.writeRaw(String.format("<!--[if lte IE 6]><link href=\"%s\" rel=\"stylesheet\" type=\"text/css\" /><![endif]-->",  script.toClientURL()));
	}
	
}
