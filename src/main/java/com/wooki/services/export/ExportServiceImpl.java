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

package com.wooki.services.export;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;

import com.wooki.services.parsers.Convertor;

public class ExportServiceImpl implements ExportService {

	private ExportInputRenderer inputRenderer;

	private Convertor toPdfConvertor;
	
	private Convertor toXHTMLConvertor;

	private Convertor toImprovedXHTML4LatexConvertor;

	private Convertor toLatexConvertor;

	public InputStream exportPdf(Long bookId) {
		if (bookId == null) {
			throw new IllegalArgumentException("Book id cannot be null to export.");
		}

		InputStream bookStream = this.inputRenderer.exportBook(bookId);
		InputStream result = toPdfConvertor.performTransformation(new InputStreamResource(bookStream));
		return result;
	}

	public InputStream exportLatex(Long bookId) {

		if (bookId == null) {
			throw new IllegalArgumentException("Book id cannot be null to export.");
		}

		/** Generate Latex */
		InputStream bookStream = this.inputRenderer.exportBook(bookId);
		InputStream xhtml = toXHTMLConvertor.performTransformation(new InputStreamResource(bookStream));
		InputStream improvedXhtml = toImprovedXHTML4LatexConvertor.performTransformation(new InputStreamResource(xhtml));
		InputStream latex = toLatexConvertor.performTransformation(new InputStreamResource(improvedXhtml));
		return latex;
	}

	public ExportInputRenderer getInputRenderer() {
		return inputRenderer;
	}

	public void setInputRenderer(ExportInputRenderer inputRenderer) {
		this.inputRenderer = inputRenderer;
	}

	public Convertor getToPdfConvertor() {
		return toPdfConvertor;
	}

	public void setToPdfConvertor(Convertor toPdfConvertor) {
		this.toPdfConvertor = toPdfConvertor;
	}

	public Convertor getToXHTMLConvertor() {
		return toXHTMLConvertor;
	}

	public void setToXHTMLConvertor(Convertor toXHTMLConvertor) {
		this.toXHTMLConvertor = toXHTMLConvertor;
	}

	public Convertor getToImprovedXHTML4LatexConvertor() {
		return toImprovedXHTML4LatexConvertor;
	}

	public void setToImprovedXHTML4LatexConvertor(Convertor toImprovedXHTML4LatexConvertor) {
		this.toImprovedXHTML4LatexConvertor = toImprovedXHTML4LatexConvertor;
	}

	public Convertor getToLatexConvertor() {
		return toLatexConvertor;
	}

	public void setToLatexConvertor(Convertor toLatexConvertor) {
		this.toLatexConvertor = toLatexConvertor;
	}

}
