package com.wooki.services.db;

import org.apache.tapestry5.ioc.ServiceBinder;

import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.ActivityDAOImpl;
import com.wooki.domain.dao.AuthorityDAO;
import com.wooki.domain.dao.AuthorityDAOImpl;
import com.wooki.domain.dao.BookDAO;
import com.wooki.domain.dao.BookDAOImpl;
import com.wooki.domain.dao.ChapterDAO;
import com.wooki.domain.dao.ChapterDAOImpl;
import com.wooki.domain.dao.CommentDAO;
import com.wooki.domain.dao.CommentDAOImpl;
import com.wooki.domain.dao.PublicationDAO;
import com.wooki.domain.dao.PublicationDAOImpl;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.dao.UserDAOImpl;
import com.wooki.services.db.query.QueryFilterService;
import com.wooki.services.db.query.QueryFilterServiceImpl;

/**
 * Defines data layer related services and utilities.
 * 
 * @author ccordenier
 */
public class DataModule
{

    public static void bind(ServiceBinder binder)
    {

        // Query builder provides convenient method to create application specific query filters.
        binder.bind(QueryFilterService.class, QueryFilterServiceImpl.class);

        // Domain dao
        binder.bind(ActivityDAO.class, ActivityDAOImpl.class);
        binder.bind(BookDAO.class, BookDAOImpl.class);
        binder.bind(ChapterDAO.class, ChapterDAOImpl.class);
        binder.bind(CommentDAO.class, CommentDAOImpl.class);
        binder.bind(PublicationDAO.class, PublicationDAOImpl.class);
        binder.bind(UserDAO.class, UserDAOImpl.class);
        binder.bind(AuthorityDAO.class, AuthorityDAOImpl.class);

    }

}
