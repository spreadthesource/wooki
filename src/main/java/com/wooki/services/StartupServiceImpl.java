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

import javax.sql.DataSource;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import com.wooki.domain.exception.AuthorizationException;
import com.wooki.domain.exception.UserAlreadyException;

public class StartupServiceImpl implements StartupService
{

    public StartupServiceImpl(ApplicationContext applicationContext,
            @Inject @Symbol(SymbolConstants.PRODUCTION_MODE) boolean productionMode, @Inject DataSource ds)
            throws UserAlreadyException, AuthorizationException
    {

        // Create Acl schema
        ClassPathResource script = new ClassPathResource("createAclSchema.sql");
        SimpleJdbcTemplate tpl = new SimpleJdbcTemplate(ds);
        SimpleJdbcTestUtils.executeSqlScript(tpl, script, true);

        script = new ClassPathResource("wookiSchema.sql");
        tpl = new SimpleJdbcTemplate(ds);
        SimpleJdbcTestUtils.executeSqlScript(tpl, script, true);
        
        script = new ClassPathResource("wookiData.sql");
        tpl = new SimpleJdbcTemplate(ds);
        SimpleJdbcTestUtils.executeSqlScript(tpl, script, true);

    }
}
