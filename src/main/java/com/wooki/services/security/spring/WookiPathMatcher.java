package com.wooki.services.security.spring;

/**
 * Define a simple matching interface. Implement this interface if you want to add a matcher to the
 * existing list. Objective is to extend the Spring security url filtering mechanism by your custom
 * URL Matchers.
 * 
 * @author ccordenier
 */
public interface WookiPathMatcher
{

    boolean matches(String url);

}
