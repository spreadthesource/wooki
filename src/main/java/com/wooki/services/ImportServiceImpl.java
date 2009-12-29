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

package com.wooki.services;

import java.io.InputStream;

import org.springframework.core.io.Resource;

import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.services.parsers.Convertor;

public class ImportServiceImpl implements ImportService {

	private BookManager bookManager;

	private ChapterManager chapterManager;

	private Convertor toHTMLConvertor;

	public InputStream importDocbook(Resource docbook) {
		/** Generate HTML */
		InputStream xhtml = toHTMLConvertor.performTransformation(docbook);
		return xhtml;
	}

	public BookManager getBookManager() {
		return bookManager;
	}

	public void setBookManager(BookManager bookManager) {
		this.bookManager = bookManager;
	}

	public ChapterManager getChapterManager() {
		return chapterManager;
	}

	public void setChapterManager(ChapterManager chapterManager) {
		this.chapterManager = chapterManager;
	}

	public Convertor getToHTMLConvertor() {
		return toHTMLConvertor;
	}

	public void setToHTMLConvertor(Convertor toHTMLConvertor) {
		this.toHTMLConvertor = toHTMLConvertor;
	}

}
