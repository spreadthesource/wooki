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

package com.wooki.components.security;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractConditional;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.wooki.services.security.WookiSecurityContext;

/**
 * Verify if the current logged user is author of the requested comment.
 * 
 * @author ccordenier
 */
public class IfAuthorOfComment extends AbstractConditional
{

    @Parameter(required = true, allowNull = false)
    private Long commentId;

    @Inject
    private WookiSecurityContext securityContext;

    /**
     * @return test parameter (if negate is false), or test parameter inverted (if negate is true)
     */
    protected boolean test()
    {
        return securityContext.isAuthorOfComment(commentId);
    }

}
