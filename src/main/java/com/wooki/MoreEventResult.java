package com.wooki;

/**
 * Used to embedd result of more action link. This structure must contain the block to render, and a
 * boolean value to mark if there is more elements left to come.
 * 
 * @author ccordenier
 */
public class MoreEventResult
{

    /**
     * The object to render at the end.
     */
    private Object renderable;

    /**
     * Indicates if there is next elements to come.
     */
    private boolean hasMore;

    public Object getRenderable()
    {
        return renderable;
    }

    public void setRenderable(Object renderable)
    {
        this.renderable = renderable;
    }

    public boolean hasMore()
    {
        return hasMore;
    }

    public void setHasMore(boolean hasMore)
    {
        this.hasMore = hasMore;
    }

}
