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

package com.wooki.domain.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic DAO will be the base for all concrete DAO implementation.
 * It will provide generic data access method. 
 *
 * @author ccordenier
 *
 * @param <T>
 * @param <PK>
 */
public interface GenericDAO<T, PK extends Serializable> {

	/** Persist the newInstance object into database */
	void create(T newInstance);

	/**
	 * Retrieve an object that was previously persisted to the database using
	 * the indicated id as primary key
	 */
	T findById(PK id);

	/** Save changes made to a persistent object. 
	 * @return TODO*/
	T update(T transientObject);

	/** Remove an object from persistent storage in the database */
	void delete(T persistentObject);

	/** List all the persistent entities of type T */
	List<T> listAll();
}
