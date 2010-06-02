package com.wooki.services;

/**
 * This interface should be implemented when you want to list service classes in an enum structure.
 * 
 * @author ccordenier
 */
public interface EnumService<T>
{

    /**
     * Get the type of the service associated to the enum element.
     * 
     * @param <T>
     * @return
     */
    public Class<T> getService();

    /**
     * Return the interface behind service implementations.
     * 
     * @return
     */
    public Class<T> getServiceInterface();

}
