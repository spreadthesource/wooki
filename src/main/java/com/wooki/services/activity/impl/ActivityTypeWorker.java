package com.wooki.services.activity.impl;

import java.lang.reflect.Modifier;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentMethodAdvice;
import org.apache.tapestry5.services.ComponentMethodInvocation;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.TransformMethodSignature;

import com.wooki.pages.ActivityDisplayBlocks;
import com.wooki.services.activity.ActivityDisplayContext;

/**
 * This workers dynamically add accessors on the ActivityDisplayBlocks page for all the available
 * type of activity to display. Doing this, developper will simply have to create the corresponding
 * display block for each new type.
 * 
 * @author ccordenier
 */
public class ActivityTypeWorker implements ComponentClassTransformWorker
{
    private final Environment environment;

    private final ClassNameLocator locator;

    public ActivityTypeWorker(Environment environment, ClassNameLocator locator)
    {
        super();
        this.environment = environment;
        this.locator = locator;
    }

    public void transform(ClassTransformation transformation, MutableComponentModel model)
    {
        if (ActivityDisplayBlocks.class.getName().equals(transformation.getClassName()))
        {
            createAccessorsForField(transformation);
        }
    }

    /**
     * Get all the activity type and create all the corresponding accessors.
     * 
     * @param transformation
     */
    private void createAccessorsForField(ClassTransformation transformation)
    {
        String packageName = "com.wooki.domain.model.activity";
        for (String type : locator.locateClassNames(packageName))
        {
            if (type.endsWith("Activity"))
            {
                String propertyName = InternalUtils.capitalize(type
                        .substring(packageName.length() + 1));
                this.addGetter(transformation, propertyName, type);
            }
        }
    }

    private void addGetter(ClassTransformation transformation, String propertyName, String type)
    {
        TransformMethodSignature getter = new TransformMethodSignature(Modifier.PUBLIC, type, "get"
                + propertyName, null, null);

        transformation.getOrCreateMethod(getter).addAdvice(new ComponentMethodAdvice()
        {
            public void advise(ComponentMethodInvocation invocation)
            {
                ActivityDisplayContext ctx = environment.peek(ActivityDisplayContext.class);
                invocation.overrideResult(ctx.getActivity());
            }
        });

    }
}