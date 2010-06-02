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

package com.wooki.test.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;

/**
 * Verify authoring management.
 * 
 * @author ccordenier
 */
@ContextConfiguration(locations =
{ "/applicationContext.xml" })
public class UserManagerTest extends AbstractTestNGSpringContextTests
{

    @Autowired
    private UserManager userManager;

    @BeforeClass
    public void initDb() throws UserAlreadyException
    {

        // Add author to the book
        User john = new User();
        john.setEmail("john.doe@gmail.com");
        john.setUsername("johndoe");
        john.setFullname("John Doe");
        john.setPassword("password");
        userManager.registerUser(john);

    }

    /**
     * Verify that we cannot create a user that already exists.
     */
    @Test
    public void checkUserExists()
    {
        User user = userManager.findByUsername("JohNDOE");
        Assert.assertNotNull(user, "John doe exist.");
    }

    /**
     * Verifies that an exception is thrown when adding an author that already exists.
     */
    @Test
    public void verifyAuthorAlreadyExists()
    {
        User john = new User();
        john.setEmail("john.doe@hotmail.com");
        john.setUsername("johndoe");
        john.setFullname("John Doe");
        john.setPassword("passpass");
        try
        {
            userManager.registerUser(john);
            Assert.fail("User john already exists, call add must raise an exception.");
        }
        catch (UserAlreadyException uaEx)
        {
            uaEx.printStackTrace();
        }
    }

    /**
     * Verify that findByUsername is case insensitive.
     */
    @Test
    public void checkFindByUserName()
    {
        User user = userManager.findByUsername("JoHnDoe");
        Assert.assertNotNull(user);
    }

}
