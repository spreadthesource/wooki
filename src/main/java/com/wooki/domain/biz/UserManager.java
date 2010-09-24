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

package com.wooki.domain.biz;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;

/**
 * Author registration and retrival manager.
 */
public interface UserManager
{

    /**
     * Add a new author to DB
     * 
     * @param user
     * @return
     * @throws UserAlreadyException
     */
    @CommitAfter
    void registerUser(User user) throws UserAlreadyException;

    /**
     * Get a user by its username.
     * 
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * Get a user by its id.
     * 
     * @param userId
     * @return
     */
    User findById(Long userId);

    /**
     * Get the list of user whos name starts with prefix.
     * 
     * @param prefix
     * @return
     */
    String[] listUserNames(String prefix);

    /**
     * update user details
     * 
     * @param user
     *            with updated details, excepting the password
     * @return
     * @throws UserAlreadyException
     * @throws AuthorizationException
     */
    @CommitAfter
    User updateDetails(User user) throws UserAlreadyException;

    /**
     * Update an user password
     * 
     * @param user
     * @return
     * @throws AuthorizationException
     */
    @CommitAfter
    User updatePassword(User user, String oldPassword, String newPassword)
            throws AuthorizationException;

    /**
     * This method force reset of password, it can only be called by administrators.
     *
     * @param user
     * @param newPassword
     * @throws AuthorizationException
     */
    @CommitAfter
    public void resetPassword(User user, String newPassword) throws AuthorizationException;
    
}
