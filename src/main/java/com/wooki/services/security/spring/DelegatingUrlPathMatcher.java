package com.wooki.services.security.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.security.web.util.RegexUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;

/**
 * This class will us to delegate url path matching to multiple implementation.
 */
public class DelegatingUrlPathMatcher implements UrlMatcher
{

    private RegexUrlPathMatcher defaultMatcher;

    private Map<String, WookiPathMatcher> matchers = new HashMap<String, WookiPathMatcher>();

    private boolean requiresLowerCaseUrl;

    public DelegatingUrlPathMatcher(Map<String, WookiPathMatcher> matchers)
    {
        this.defaultMatcher = new RegexUrlPathMatcher();
        if (matchers != null)
        {
            this.matchers.putAll(matchers);
        }
    }

    public Object compile(String urlPattern)
    {
        if (matchers.containsKey(urlPattern)) { return matchers.get(urlPattern); }
        return this.defaultMatcher.compile(urlPattern);
    }

    public String getUniversalMatchPattern()
    {
        return this.defaultMatcher.getUniversalMatchPattern();
    }

    public boolean pathMatchesUrl(Object compiledUrlPattern, String url)
    {
        if (compiledUrlPattern instanceof Pattern)
        {
            Pattern pattern = (Pattern) compiledUrlPattern;
            return pattern.matcher(url).matches();
        }
        WookiPathMatcher matcher = (WookiPathMatcher) compiledUrlPattern;
        return matcher.matches(url);
    }

    public boolean requiresLowerCaseUrl()
    {
        return this.requiresLowerCaseUrl;
    }

    public void setRequiresLowerCaseUrl(boolean requiresLowerCaseUrl)
    {
        this.defaultMatcher.setRequiresLowerCaseUrl(requiresLowerCaseUrl);
        this.requiresLowerCaseUrl = requiresLowerCaseUrl;
    }

}
