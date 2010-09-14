package com.wooki.pages.book;

import java.io.InputStream;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import com.wooki.base.BookBase;
import com.wooki.services.BookStreamResponse;
import com.wooki.services.export.ExportService;

/**
 * Simply generates the PDF in activation phase.
 *
 * @author ccordenier
 *
 */
public class Pdf extends BookBase
{

    @Inject
    private Logger logger;

    @Inject
    private ExportService exportService;

    @Inject
    private Messages messages;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String[] errors;

    /**
     * Simply export to PDF.
     * 
     * @return
     */
    @Override
    @OnEvent(value = EventConstants.ACTIVATE)
    public Object setupBookBase(Long bookId)
    {
        try
        {
            super.setupBookBase(bookId);
            InputStream bookStream = exportService.exportPdf(bookId);
            return new BookStreamResponse(getBook().getSlugTitle(), bookStream);
        }
        catch (Exception ex)
        {
            errors = new String[]
            { messages.get("print-error") };
            logger.error("An Error has occured during PDF generation", ex);
            return this;
        }
    }

}
