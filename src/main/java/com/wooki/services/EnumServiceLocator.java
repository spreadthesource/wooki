package com.wooki.services;

/**
 * This service allows to implement a lookup strategy in function of an enum type.
 * 
 * @author ccordenier
 */
public interface EnumServiceLocator
{
    /**
     * This will return an instance of a service in fonction of an {@link EnumService} value. More
     * generally, all this value will contain the type of service to service, the id of the service
     * will be equal to the String representation of the enum type.
     * 
     * @param <T>
     * @param type
     * @return
     */
    <T> T getService(EnumService<T> type);
}
