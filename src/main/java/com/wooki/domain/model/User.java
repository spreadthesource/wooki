//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.domain.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Wooki basic user. Can post comment, write book etc.
 */
@Entity
@Table(name = "Users")
public class User extends WookiEntity implements UserDetails
{

    private static final long serialVersionUID = 4060967693790504175L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "users")
    private List<Book> books;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "UsersAuthorities", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns =
    { @JoinColumn(name = "authority_id") })
    private List<Authority> authorities = new LinkedList<Authority>();

    @Column(name = "password")
    private String password;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }

    public String getFullname()
    {
        return fullname;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public List<Book> getBooks()
    {
        return books;
    }

    public void setBooks(List<Book> books)
    {
        this.books = books;
    }

    public List<GrantedAuthority> getAuthorities()
    {
        if (this.authorities != null)
        {
            ArrayList<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
            for (Authority auth : this.authorities)
            {
                result.add(new GrantedAuthorityImpl(auth.getAuthority()));
            }
            return result;
        }
        return null;
    }

    public void setGrantedAuthorities(List<Authority> grantedAuthorities)
    {
        this.authorities = grantedAuthorities;
    }

    public boolean isAccountNonExpired()
    {
        return true;
    }

    public boolean isAccountNonLocked()
    {
        return true;
    }

    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    public boolean isEnabled()
    {
        return true;
    }

}
