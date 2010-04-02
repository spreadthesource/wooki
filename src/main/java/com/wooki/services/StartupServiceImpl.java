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

package com.wooki.services;

import static org.apache.commons.io.FilenameUtils.separatorsToUnix;
import static org.apache.commons.lang.StringUtils.substring;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.Context;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import com.carbonfive.db.jdbc.DatabaseType;
import com.carbonfive.db.jdbc.DatabaseUtils;
import com.carbonfive.db.jdbc.schema.CreateDatabase;
import com.carbonfive.db.jdbc.schema.DropDatabase;
import com.carbonfive.db.migration.DriverManagerMigrationManager;
import com.carbonfive.db.migration.ResourceMigrationResolver;
import com.carbonfive.db.migration.SimpleVersionStrategy;
import com.wooki.WookiSymbolsConstants;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;

public class StartupServiceImpl implements StartupService
{
    private Context context;

    private Logger logger;

    private String migrationsPath;

    private Session session;

    private BookManager bookManager;

    private ChapterManager chapterManager;

    private UserManager userManager;
    
    private String driver;
    
    private String url;
    
    private String username;
    
    private String password;
    

    public StartupServiceImpl(Logger logger, ApplicationContext applicationContext,
            @Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode,
            @Inject @Symbol(WookiSymbolsConstants.MIGRATIONS_PATH) String migrationsPath,
            Context context, BookManager bookManager, ChapterManager chapterManager,
            UserManager userManager, Session session) throws UserAlreadyException,
            AuthorizationException
    {
        this.session = session;
        this.logger = logger;
        this.bookManager = bookManager;
        this.chapterManager = chapterManager;
        this.userManager = userManager;
        this.migrationsPath = migrationsPath;
        this.context = context;
        
        Configuration configuration = new Configuration();
        configuration.configure();

    }
}
