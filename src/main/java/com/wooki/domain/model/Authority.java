/* Copyright (c) 2008, DENKSOFT SRL. All rights reserved.
     This software is licensed under the BSD license available at
     http://www.opensource.org/licenses/bsd-license.php, with these parameters:
     <OWNER> = DENKSOFT SRL <ORGANIZATION> = DENKSOFT SRL <YEAR> = 2008
 */

package com.wooki.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;

@Entity
public class Authority implements Serializable
{

    @Id
    @GeneratedValue
    private long id;

    @ManyToMany
    private List<User> users = new LinkedList<User>();

    private String authority;

    public Authority()
    {
    }

    public Authority(String authority)
    {
        this.authority = authority;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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

}
