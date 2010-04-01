package com.wooki.services.security;

import com.wooki.domain.model.User;

public class WookiSecurityContextMock implements WookiSecurityContext
{
    
    private User user;
    
    public User getAuthor()
    {
        return user;
    }

    public String getUsername()
    {
        return user.getUsername();
    }

    public boolean isAuthorOfBook(Long bookId)
    {
        return true;
    }

    public boolean isAuthorOfChapter(Long chapterId)
    {
        return true;
    }

    public boolean isAuthorOfComment(Long commentId)
    {
        return true;
    }

    public boolean isLoggedIn()
    {
        return true;
    }

    public boolean isOwnerOfBook(Long bookId)
    {
        return true;
    }

    public void log(User user)
    {   
        this.user = user;
    }

}
