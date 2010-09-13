package com.wooki.components;

import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import com.wooki.domain.model.Chapter;

/**
 * Display the list of comments and links on corresponding chapter.
 * 
 * @author ccordenier
 */
public class Comments
{

    @Property
    @Parameter(required = true, allowNull = false)
    private long bookId;

    @Property
    @Parameter
    private List<Chapter> chapters;

    @Property
    private Chapter chapter;

    @Property
    private int loopIdx;

    public Object[] getChapterCtx()
    {
        return new Object[]
        { bookId, this.chapter.getId() };
    }

}
