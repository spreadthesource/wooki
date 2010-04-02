package com.spreadthesource.tapestry5.installer;

import javax.servlet.ServletContext;

import org.apache.tapestry5.internal.spring.SpringModuleDef;
import org.apache.tapestry5.ioc.def.ModuleDef;

public class TapestrySpringDelayedFilter extends TapestryDelayedFilter
{

    @Override
    protected ModuleDef[] provideExtraModuleDefs(ServletContext context)
    {
        return new ModuleDef[] {
                new SpringModuleDef(context)
        };
    }

    
}
