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

package com.wooki.services.security;

import com.wooki.domain.model.User;
import com.wooki.domain.model.WookiEntity;

/**
 * Contains authorization and security behavior of wooki.
 * 
 * @author ccordenier
 */
public interface WookiSecurityContext
{

    void log(User user);

    /**
     * Simply clear the security context.
     *
     */
    void logout();
    
    /**
     * Check if a user is logged in.
     * 
     * @return
     */
    boolean isLoggedIn();

    /**
     * Get the logged username : No DB access, only the security context is accessed.
     * 
     * @return
     */
    String getUsername();

    /**
     * Get the name of the logged user.
     * 
     * @return
     */
    User getUser();

    /**
     * Check if the current logged user has administration permission on a given entity.
     * 
     * @param id
     * @return
     */
    boolean isOwner(WookiEntity object);

    /**
     * A user will be allowed to write to the entity if he has WRITE or ADMINISTRATION permission.
     *
     * @param id
     * @return
     */
    boolean canWrite(WookiEntity object);
    
    /**
     * A user will be allowed to delete to the entity if he has DELETE or ADMINISTRATION permission.
     *
     * @param id
     * @return
     */
    boolean canDelete(WookiEntity object);

}
