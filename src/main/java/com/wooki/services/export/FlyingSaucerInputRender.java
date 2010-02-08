package com.wooki.services.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletContext;

import org.apache.tapestry5.ContentType;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.internal.services.ArrayEventContext;
import org.apache.tapestry5.internal.services.HeartbeatImpl;
import org.apache.tapestry5.internal.services.RenderQueueImpl;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.LoggerSource;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.Heartbeat;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.slf4j.Logger;
import org.springframework.web.context.ServletContextAware;

/**
 * Generate a representation to generate PDF through FlyingSaucer.
 * 
 * @author ccordenier
 * 
 */
public class FlyingSaucerInputRender implements ExportInputRenderer, ServletContextAware {

	private ServletContext servletContext;

	private RequestPageCache pageCache;

	private TypeCoercer typeCoercer;

	private LoggerSource loggerSource;

	private MarkupWriterFactory factory;

	private Environment environment;

	private String encoding;

	private boolean initDone;

	/**
	 * Init Tapestry registries and services.
	 * 
	 */
	private void init() {
		if (initDone) {
			return;
		}
		Registry tapestryRegistry = (Registry) this.servletContext.getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
		this.encoding = tapestryRegistry.getService(SymbolSource.class).valueForSymbol(SymbolConstants.CHARSET);
		this.environment = tapestryRegistry.getService(Environment.class);
		this.factory = tapestryRegistry.getService(MarkupWriterFactory.class);
		this.loggerSource = tapestryRegistry.getService(LoggerSource.class);
		this.pageCache = tapestryRegistry.getService(RequestPageCache.class);
		this.typeCoercer = tapestryRegistry.getService(TypeCoercer.class);
		this.initDone = true;
	}

	public InputStream exportBook(Long bookId) {

		this.init();

		MarkupWriter writer = factory.newMarkupWriter(new ContentType("text/xml", encoding));

		Page full = pageCache.get("book/fullFlyingSaucer");
		full.getRootElement().triggerContextEvent(EventConstants.ACTIVATE, new ArrayEventContext(typeCoercer, 1), null);
		String name = "wooki.render." + full.getLogger().getName();
		Logger logger = loggerSource.getLogger(name);

		try {

			this.environment.push(Heartbeat.class, new HeartbeatImpl());
			RenderQueueImpl queue = new RenderQueueImpl(logger);
			queue.push(full.getRootElement());
			queue.run(writer);

			ByteArrayOutputStream dos = new ByteArrayOutputStream();
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(dos, encoding));
			writer.toMarkup(printWriter);
			dos.flush();
			printWriter.flush();

			return new ByteArrayInputStream(dos.toByteArray());

		} catch (Exception ex) {
			logger.debug("Error while rendering book for FlyingSaucer", ex);
			throw new TapestryException("Error while rendering book for FlyingSaucer", ex);
		} finally {
			this.environment.pop(Heartbeat.class);
		}

	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
