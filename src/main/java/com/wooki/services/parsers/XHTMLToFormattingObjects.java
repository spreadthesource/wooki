package com.wooki.services.parsers;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XHTMLToFormattingObjects implements Convertor, URIResolver,
		EntityResolver {
	private TransformerFactory tFactory = TransformerFactory.newInstance();
	private Transformer transformer;
	private Resource xslStylesheet;
	private HttpClient httpClient;
	// Cache management
	private CacheManager cacheManager;

	private String cacheName;
	Cache cache;
	private Logger logger = Logger.getLogger(XHTMLToFormattingObjects.class);

	/**
	 * This method is used to init the cache
	 */
	@PostConstruct
	public void initCache() {
		logger.debug("Start to init " + cacheName + " cache");
		cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			throw new IllegalArgumentException("Cache " + cacheName
					+ " does not exist");
		}
		putInCache("/xhtml1-strict.dtd",
				"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
		putInCache("/xhtml-lat1.ent",
				"http://www.w3.org/TR/xhtml1/DTD/xhtml-lat1.ent");
		putInCache("/xhtml-symbol.ent",
				"http://www.w3.org/TR/xhtml1/DTD/xhtml-symbol.ent");
		putInCache("/xhtml-special.ent",
				"http://www.w3.org/TR/xhtml1/DTD/xhtml-special.ent");

		tFactory.setURIResolver(this);
		logger.debug("Init of " + cacheName + " is finished!");
	}

	private void putInCache(String fileLocation, String systemId) {
		try {
			InputStream in = this.getClass().getResourceAsStream(fileLocation);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			byte[] input = null;
			int available = 0;
			while ((available = in.available()) > 0) {
				int result;
				input = new byte[available];
				result = in.read(input);
				baos.write(input);
				if (result == -1)
					break;
			}
			byte[] dtd = baos.toByteArray();
			Element element = new Element(systemId, dtd);
			cache.put(element);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Didn't manage to put " + fileLocation + " in the \""
					+ cacheName + "\" cache for systemId " + systemId);
			logger.error(e.getLocalizedMessage());
		}
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public Resource getXslStylesheet() {
		return xslStylesheet;
	}

	public void setXslStylesheet(Resource xslStylesheet) {
		this.xslStylesheet = xslStylesheet;
	}

	public InputStream performTransformation(Resource xmlDocument) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		StreamResult toReturn = new StreamResult(baos);

		// Use the TransformerFactory to instantiate a Transformer that will
		// work with the stylesheet you specify. This method call also processes
		// the stylesheet into
		// a compiled Templates object.

		try {
			Element processedStylesheet = cache
					.get(this.xslStylesheet.getURL());
			if (processedStylesheet == null) {
				StreamSource input = new StreamSource(this.xslStylesheet
						.getInputStream());
				input.setSystemId(this.xslStylesheet.getFile());
				transformer = tFactory.newTransformer(input);
				transformer.setURIResolver(this);
				processedStylesheet = new Element(this.xslStylesheet.getURL(),
						transformer);
				cache.put(processedStylesheet);
			} else {
				transformer = (Transformer) processedStylesheet
						.getObjectValue();
			}
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setEntityResolver(this);

			// transformer.
			// Use the Transformer to apply the associated Templates object to
			// an
			// XML document (foo.xml) and write the output to a file.
			transformer.transform(new SAXSource(reader, new InputSource(
					xmlDocument.getInputStream())), toReturn);
			return new ByteArrayInputStream(baos.toByteArray());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} catch (TransformerException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			return null;
		} catch (Error e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return new ByteArrayInputStream(baos.toByteArray());
	}

	public Source resolve(String href, String base) throws TransformerException {
		Source toReturn = null;
		if (base != null && base.contains("file://")) {
			String newPath = base.substring("file:///".length()).replace('/',
					File.separatorChar);
			newPath = newPath.substring(0, newPath
					.lastIndexOf(File.separatorChar));
			File baseFile = new File(newPath);
			baseFile = new File(baseFile.getAbsolutePath() + File.separatorChar
					+ href.replace('/', File.separatorChar));
			try {
				toReturn = new StreamSource(new FileInputStream(baseFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				logger.error(e.getLocalizedMessage());
				return null;
			}
		} else {
			toReturn = new StreamSource(XHTMLToFormattingObjects.class
					.getResourceAsStream(href));
		}
		return toReturn;
	}

	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		InputSource toReturn = null;
		Element cachedValue = cache.get(systemId);
		if (cachedValue == null) {
			if (systemId.contains("http://")) {
				HttpMethod get = new GetMethod(systemId);
				httpClient.executeMethod(get);
				byte[] body = get.getResponseBody();
				Element element = new Element(systemId, body);
				cache.put(element);
				toReturn = new InputSource(new BufferedInputStream(
						new ByteArrayInputStream(body)));
				toReturn.setSystemId(systemId);
			} else if (systemId.contains("file://")) {
				String newPath = systemId.substring("file:///".length())
						.replace('/', File.separatorChar);
				File baseFile = new File(newPath);
				try {
					toReturn = new InputSource(new FileInputStream(baseFile));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					logger.error(e.getLocalizedMessage());
					return null;
				}
			}
		} else {
			toReturn = new InputSource(new BufferedInputStream(
					new ByteArrayInputStream((byte[]) cachedValue
							.getObjectValue())));
			toReturn.setSystemId(systemId);
		}
		return toReturn;
	}

}