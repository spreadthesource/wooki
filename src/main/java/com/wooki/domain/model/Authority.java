/* Copyright (c) 2008, DENKSOFT SRL. All rights reserved.
     This software is licensed under the BSD license available at
     http://www.opensource.org/licenses/bsd-license.php, with these parameters:
     <OWNER> = DENKSOFT SRL <ORGANIZATION> = DENKSOFT SRL <YEAR> = 2008
 */

package com.wooki.domain.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Authority")
public class Authority extends WookiEntity implements Serializable
{
    private static final long serialVersionUID = -4698804116621759012L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @ManyToMany
    @JoinTable(name = "AuthorityUser", joinColumns = @JoinColumn(name = "authority_id"), inverseJoinColumns =
    { @JoinColumn(name = "user_id") })
    private List<User> users = new LinkedList<User>();

    private String authority;

    public Authority()
    {
    }

    public Authority(String authority)
    {
        this.authority = authority;
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

    public String getAuthority()
    {
        return authority;
    }

    public void setAuthority(String authority)
    {
        this.authority = authority;
    }

    public Long getId()
    {
        return this.id;
    }

}
