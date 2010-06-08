package com.wooki.components;

import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractConditional;
import org.apache.tapestry5.corelib.components.Unless;

/**
 * Check the nullity of a property.
 * 
 * @author ccordenier
 */
public class IfNotEmpty extends AbstractConditional
{

    /**
     * If true, then the body of the If component is rendered. If false, the body is omitted.
     */
    @Parameter(required = true)
    private List test;

    /**
     * Optional parameter to invert the test. If true, then the body is rendered when the test
     * parameter is false (not true).
     * 
     * @see Unless
     */
    @Parameter
    private boolean negate;

    /**
     * @return test parameter (if negate is false), or test parameter inverted (if negate is true)
     */
    protected boolean test()
    {
        if (negate) { return test == null || test.size() == 0; }
        return test != null && test.size() > 0;
    }
}
