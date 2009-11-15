package com.wooki.services.parsers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

public class FormattingObjectsToPDF implements Convertor {
	private TransformerFactory factory = TransformerFactory.newInstance();
	private FopFactory fopFactory = FopFactory.newInstance();

	private Logger logger = Logger.getLogger(FormattingObjectsToPDF.class);
	
	public InputStream performTransformation(Resource xmlDocument) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			// Step 3: Construct fop with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, baos);

			// Step 4: Setup JAXP using identity transformer
			// identity transformer
			Transformer transformer = factory.newTransformer();

			// Step 5: Setup input and output for XSLT transformation
			// Setup input stream
			Source src = new StreamSource(xmlDocument.getInputStream());

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Step 6: Start XSLT transformation and FOP processing
			transformer.transform(src, res);

		} catch (FOPException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} catch (TransformerException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} finally {
			// Clean-up
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getLocalizedMessage());
			}
		}
		return new ByteArrayInputStream(baos.toByteArray());
	}

}