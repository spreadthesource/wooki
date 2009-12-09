package com.wooki.services;

import java.io.InputStream;

/**
 * Export book service, will provide different format, pdf...
 *
 * @author ccordenier
 *
 */
public interface ExportService {

	/**
	 * Export book to PDF.
	 *
	 * @param bookId
	 * @return
	 */
	InputStream exportPdf(Long bookId);
	
}
