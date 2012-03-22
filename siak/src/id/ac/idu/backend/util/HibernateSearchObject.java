/**
 * Copyright 2010 the original author or authors.
 *
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package id.ac.idu.backend.util;

import com.trg.search.Search;

import java.io.Serializable;

/**
 * EN: SearchObject depending on the Search Object from the
 * Hibernate-Generic-DAO framework. <br>
 * DE: SearchObject aufbauend auf dem Search object des
 * <b>Hibernate-Generic-DAO</b> frameworks.<br>
 *
 * @param <E>
 * @author bbruhns
 * @author sgerth
 * @see http://code.google.com/p/hibernate-generic-dao/ <br>
 *      Many thanks to David Wolverton.
 */
public class HibernateSearchObject<E> extends Search implements Serializable {

    private static final long serialVersionUID = 1L;

    public HibernateSearchObject(Class<E> entityClass) {
        super(entityClass);
    }

    public HibernateSearchObject(Class<E> entityClass, int pageSize) {
        super(entityClass);
        setFirstResult(0);
        setMaxResults(pageSize);
    }
}
