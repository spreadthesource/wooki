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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ibm.icu.util.Calendar;
import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.Authority;
import com.wooki.domain.model.User;
import com.wooki.domain.model.WookiGrantedAuthority;
import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.AccountEventType;
import com.wooki.services.security.WookiSecurityContext;

public class UserManagerImpl implements UserManager
{

    private AclCache aclCache;

    private final UserDAO userDao;

    private final ActivityDAO activityDao;

    private final WookiSecurityContext securityCtx;

    private final SecurityManager securityManager;

    private final SaltSource saltSource;

    private final PasswordEncoder passwordEncoder;

    private final AclPermissionEvaluator aclPermissionEvaluator;

    public UserManagerImpl(UserDAO userDAO, ActivityDAO activityDAO,
            ApplicationContext applicationContext)
    {
        this.userDao = userDAO;
        this.activityDao = activityDAO;

        this.securityCtx = applicationContext.getBean(WookiSecurityContext.class);
        this.saltSource = applicationContext.getBean(SaltSource.class);
        this.passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        this.aclPermissionEvaluator = applicationContext.getBean(AclPermissionEvaluator.class);
        this.securityManager = applicationContext.getBean(SecurityManager.class);
        this.aclCache = applicationContext.getBean(AclCache.class);
    }

    public void registerUser(User author) throws UserAlreadyException
    {

        if (findByUsername(author.getUsername()) != null) { throw new UserAlreadyException(); }

        // Encode password into database
        String pass = author.getPassword();
        author.setCreationDate(new Date());
        author.setPassword(this.passwordEncoder.encodePassword(pass, this.saltSource
                .getSalt(author)));

        // Add default Author Role
        author.setGrantedAuthorities(Arrays.asList(new Authority[]
        { this.securityManager.getOrCreateAuthority(WookiGrantedAuthority.ROLE_AUTHOR
                .getAuthority()) }));

        userDao.create(author);

        AccountActivity aa = new AccountActivity();
        aa.setCreationDate(Calendar.getInstance().getTime());
        aa.setType(AccountEventType.JOIN);
        aa.setUser(author);
        this.activityDao.create(aa);

        // Set permission
        this.securityCtx.log(author);
        this.securityManager.setOwnerPermission(author);

    }

    public User findByUsername(String username)
    {
        return userDao.findByUsername(username);
    }

    public User findById(Long userId)
    {
        assert userId != null;
        return userDao.findById(userId);
    }

    public String[] listUserNames(String prefix)
    {
        return userDao.listUserNames(prefix);
    }

    public User updateDetails(User user) throws UserAlreadyException
    {
        assert user != null;

        if (!this.securityCtx.isLoggedIn()
                || !this.aclPermissionEvaluator.hasPermission(SecurityContextHolder.getContext()
                        .getAuthentication(), user, BasePermission.ADMINISTRATION)) { throw new AuthorizationException(
                "Action not authorized"); }

        User userByUsername = findByUsername(user.getUsername());

        // check if the new username is not already taken by someone else
        if (userByUsername != null && userByUsername.getId() != user.getId())
        {
            // Reset user state and throw an exception
            userDao.refresh(user);
            throw new UserAlreadyException();
        }

        // Update sid
        BigInteger sidId = userDao.findSid(securityCtx.getUsername());
        if (sidId != null)
        {
            userDao.updateSid(sidId, user.getUsername());
        }

        // Force re-log
        this.securityCtx.log(user);
        userDao.update(user);

        // TODO Not satisfying, we should clear only the Acl for user object
        aclCache.clearCache();

        return user;
    }

    public void resetPassword(User user, String newPassword)
    {
        assert user != null;
        assert newPassword != null;

        if (!securityCtx.hasAuthority(WookiGrantedAuthority.ROLE_ADMIN)) { throw new AuthorizationException(); }

        user.setPassword(this.passwordEncoder.encodePassword(newPassword, this.saltSource
                .getSalt(user)));
        userDao.update(user);
    }

    public User updatePassword(User user, String oldPassword, String newPassword)
            throws AuthorizationException
    {
        assert user != null;
        assert oldPassword != null;
        assert newPassword != null;

        // Check access
        if (this.aclPermissionEvaluator.hasPermission(SecurityContextHolder.getContext()
                .getAuthentication(), user, BasePermission.ADMINISTRATION))
        {
            // In case of admin rights, bypass previous old password checking
            if (!securityCtx.hasAuthority(WookiGrantedAuthority.ROLE_ADMIN))
            {
                String encodedPassword = this.passwordEncoder.encodePassword(
                        oldPassword,
                        this.saltSource.getSalt(user));
                if (!encodedPassword.equals(this.securityCtx.getUser().getPassword())) { throw new AuthorizationException(); }
            }

            user.setPassword(this.passwordEncoder.encodePassword(newPassword, this.saltSource
                    .getSalt(user)));
            this.securityCtx.log(userDao.update(user));
            return user;
        }
        else
        {
            throw new AuthorizationException("You do not have enough rights to do this action");
        }
    }
}
