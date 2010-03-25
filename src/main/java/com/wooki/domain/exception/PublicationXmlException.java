package com.wooki.domain.exception;

/**
 * This exception is thrown when the an error occurs during publication process. In case of
 * publication exception, the document must be saved but not published.
 * 
 * @author ccordenier
 */
public class PublicationXmlException extends RuntimeException
{

    private static final long serialVersionUID = 6293585777634167048L;

    public PublicationXmlException()
    {
        super();
    }

    public PublicationXmlException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PublicationXmlException(String message)
    {
        super(message);
    }

    public PublicationXmlException(Throwable cause)
    {
        super(cause);
    }

}
