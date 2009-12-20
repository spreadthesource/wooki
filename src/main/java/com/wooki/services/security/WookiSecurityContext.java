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

/**
 * Contains authorization and security behavior of wooki.
 *
 * @author ccordenier
 *
 */
public interface WookiSecurityContext {

	void log(User user);
	
	/**
	 * Check if a user is logged in.
	 *
	 * @return
	 */
	boolean isLoggedIn();
	
	/**
	 * Get the name of the logged user.
	 *
	 * @return
	 */
	User getAuthor();
	
	/**
	 * Check if the logged in user is author of a book.
	 *
	 * @param bookId
	 * @return
	 */
	boolean isAuthorOfBook(Long bookId);
	
	/**
	 * Check if the user is author of a comment
	 */
	boolean isAuthorOfComment(Long commentId);
	
}
