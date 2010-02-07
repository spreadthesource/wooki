package com.wooki.services.export;

import java.io.InputStream;

/**
 * This interface is used to define services that will render Book for a given
 * export service.
 * 
 * @author ccordenier
 * 
 */
public interface ExportInputRenderer {

	/**
	 * Render a printable representation of a book.
	 * 
	 * @param bookId
	 * @return
	 */
	public InputStream exportBook(Long bookId);

}
