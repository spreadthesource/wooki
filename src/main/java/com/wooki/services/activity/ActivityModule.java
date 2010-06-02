package com.wooki.services.activity;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.ComponentClassTransformWorker;

import com.wooki.services.activity.impl.ActivityBlockSourceImpl;
import com.wooki.services.activity.impl.ActivityTypeWorker;

/**
 * Defines all the service related to Activities.
 * 
 * @author ccordenier
 */
public class ActivityModule
{

    public static void bind(ServiceBinder binder)
    {
        binder.bind(ActivityBlockSource.class, ActivityBlockSourceImpl.class);

        // Bind all all the activity sources.
        for (ActivitySourceType type : ActivitySourceType.values())
        {
            binder.bind(ActivitySource.class, type.getService()).withId(type.toString());
        }
    }

    /**
     * Add worker needed to handle activity polymorphism.
     * 
     * @param configuration
     */
    public static void contributeComponentClassTransformWorker(
            OrderedConfiguration<ComponentClassTransformWorker> configuration)
    {
        configuration.addInstance("Activities", ActivityTypeWorker.class, "after:Component");
    }

}
