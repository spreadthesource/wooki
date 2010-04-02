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

import java.util.Arrays;
import java.util.Date;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.internal.util.Defense;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.ResourceHolderSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

    private final UserDAO userDao;

    private final ActivityDAO activityDao;

    private final WookiSecurityContext securityCtx;

    private final SecurityManager securityManager;

    private final SaltSource saltSource;

    private final PasswordEncoder passwordEncoder;

    private final AclPermissionEvaluator aclPermissionEvaluator;

    private final HibernateSessionManager sessionManager;

    public UserManagerImpl(UserDAO userDAO, ActivityDAO activityDAO,
            ApplicationContext applicationContext, HibernateSessionManager sessionManager)
    {
        this.userDao = userDAO;
        this.activityDao = activityDAO;
        this.sessionManager = sessionManager;

        this.securityCtx = applicationContext.getBean(WookiSecurityContext.class);
        this.saltSource = applicationContext.getBean(SaltSource.class);
        this.passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        this.aclPermissionEvaluator = applicationContext.getBean(AclPermissionEvaluator.class);
        this.securityManager = applicationContext.getBean(SecurityManager.class);
    }

    private static class ConnectionSynchronization extends
            ResourceHolderSynchronization<SessionHolder, SessionFactory>
    {

        public ConnectionSynchronization(SessionHolder connectionHolder,
                SessionFactory connectionFactory)
        {
            super(connectionHolder, connectionFactory);
        }

       
    }

    public void addUser(User author) throws UserAlreadyException
    {
        
        Session session = this.sessionManager.getSession();
        System.out.println(session.getTransaction().isActive());
        TransactionSynchronizationManager.initSynchronization();
        TransactionSynchronizationManager.registerSynchronization(new ConnectionSynchronization(new SessionHolder(session), session.getSessionFactory()));
        // SessionFactoryUtils.doGetSession(this.sessionManager.getSession().getSessionFactory(), false);

        if (findByUsername(author.getUsername()) != null) { throw new UserAlreadyException(); }

        // Encode password into database
        String pass = author.getPassword();
        author.setCreationDate(new Date());
        author.setPassword(this.passwordEncoder.encodePassword(pass, this.saltSource
                .getSalt(author)));

        // Add default Author Role
        author.setGrantedAuthorities(Arrays.asList(new Authority[]
        { new Authority(WookiGrantedAuthority.ROLE_AUTHOR.getAuthority()) }));

        userDao.create(author);

        AccountActivity aa = new AccountActivity();
        aa.setCreationDate(Calendar.getInstance().getTime());
        aa.setType(AccountEventType.JOIN);
        aa.setUser(author);
        this.activityDao.create(aa);

        // Set security context
        securityCtx.log(author);

        // Set permission
        this.securityManager.setOwnerPermission(author);

    }

    public User findByUsername(String username)
    {
        return userDao.findByUsername(username);
    }

    public User findById(Long userId)
    {
        Defense.notNull(userId, "userId");
        return userDao.findById(userId);
    }

    public String[] listUserNames(String prefix)
    {
        return userDao.listUserNames(prefix);
    }

    public User updateDetails(User user) throws AuthorizationException, UserAlreadyException
    {
        Defense.notNull(user, "user");

        if (!this.securityCtx.isLoggedIn() || this.securityCtx.getUser().getId() != user.getId()
                && user.getPassword() != this.securityCtx.getUser().getPassword()) { throw new AuthorizationException(
                "Action not authorized"); }

        User userByUsername = findByUsername(user.getUsername());

        // check if the new username is not already taken by someone else
        if (userByUsername != null && userByUsername.getId() != user.getId()) { throw new UserAlreadyException(); }

        this.securityCtx.log(userDao.update(user));

        return user;
    }

    public User updatePassword(User user, String oldPassword, String newPassword)
            throws AuthorizationException
    {
        Defense.notNull(user, "user");
        Defense.notNull(oldPassword, "oldPassword");
        Defense.notNull(newPassword, "newPassword");

        // Check access
        if (this.aclPermissionEvaluator.hasPermission(SecurityContextHolder.getContext()
                .getAuthentication(), user, BasePermission.ADMINISTRATION))
        {
            String encodedPassword = this.passwordEncoder.encodePassword(
                    oldPassword,
                    this.saltSource.getSalt(user));
            if (!encodedPassword.equals(this.securityCtx.getUser().getPassword())) { throw new AuthorizationException(); }

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
