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

package com.wooki.domain.exception;

/**
 * Used to raise authentication related exception.
 * 
 * @author ccordenier
 */
public class AuthorizationException extends RuntimeException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 5440438345681985807L;

    public AuthorizationException()
    {
        super();
    }

    public AuthorizationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public AuthorizationException(String message)
    {
        super(message);
    }

    public AuthorizationException(Throwable cause)
    {
        super(cause);
    }

}
