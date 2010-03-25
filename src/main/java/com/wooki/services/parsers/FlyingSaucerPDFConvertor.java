package com.wooki.services.parsers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * Use flying saucer to generate a PDF from a given source.
 * 
 * @author ccordenier
 */
public class FlyingSaucerPDFConvertor implements Convertor
{

    public InputStream performTransformation(Resource xmlDocument)
    {
        try
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(xmlDocument.getInputStream());
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(doc, null);
            renderer.layout();
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            renderer.createPDF(os);
            os.close();
            return new ByteArrayInputStream(os.toByteArray());
        }
        catch (Exception ex)
        {
            throw new TapestryException("Error during PDF generation", ex);
        }
    }

}
