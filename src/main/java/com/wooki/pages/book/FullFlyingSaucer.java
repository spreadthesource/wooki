//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.pages.book;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Meta;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;
import org.slf4j.Logger;

import com.wooki.domain.biz.ChapterManager;
import com.wooki.services.parsers.DOMManager;

/**
 * This page displays a book with its table of contents.
 */
@Meta(value = { "content-type=text/xml" })
public class FullFlyingSaucer extends Index {

	@Inject
	private Context context;

	@Inject
	private ChapterManager chapterManager;

	@Inject
	private Logger logger;

	@Inject
	private DOMManager domManager;

	@Property
	private int chapterIdx;

	public String getPrintCssPath() {
		return this.context.getRealFile("/static/css/print.css").getAbsolutePath();
	}

	public String getLastPublishedContent() {
		return this.chapterManager.getLastPublishedContent(this.getCurrentChapter().getId());
	}

	@AfterRender
	public void generateBookmarks(MarkupWriter writer) {
		Element body = writer.getDocument().getElementById("content");
		if (body == null) {
			logger.error("An errors has occured during bookmarks generation.");
			return;
		}
		
		// Generate and add bookmarks
		String bookmarksDom = this.domManager.generatePdfBookmarks(body.toString(), 2, 4);
		if (bookmarksDom != null && !"".equals(bookmarksDom)) {
			Element bookmarks = writer.getDocument().find("html/head").element("bookmarks");
			bookmarks.raw(bookmarksDom);
		}
	}
}
