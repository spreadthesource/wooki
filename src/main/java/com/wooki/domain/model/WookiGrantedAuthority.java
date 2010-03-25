package com.wooki.domain.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * This enum defines the list of granted authorities used in Wooki Application.
 * 
 * @author ccordenier
 */
public enum WookiGrantedAuthority implements GrantedAuthority
{

    ROLE_AUTHOR, ROLE_ADMIN;

    public String getAuthority()
    {
        return this.toString();
    }

}
