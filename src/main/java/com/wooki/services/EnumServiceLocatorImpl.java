package com.wooki.services;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.services.Context;

/**
 * This implementation retrieve service instances from the Tapestry registry contained in the
 * servlet Context.
 * 
 * @author ccordenier
 */
public class EnumServiceLocatorImpl implements EnumServiceLocator
{

    private final Context context;

    private final Registry registry;

    public EnumServiceLocatorImpl(Context context)
    {
        super();
        this.context = context;
        this.registry = (Registry) this.context.getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
    }

    public <T> T getService(EnumService<T> type)
    {
        return registry.getService(type.toString(), type.getServiceInterface());
    }

}
