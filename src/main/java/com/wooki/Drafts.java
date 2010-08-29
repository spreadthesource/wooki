package com.wooki;

import java.util.Hashtable;
import java.util.Map;

import com.wooki.domain.model.Chapter;

public class Drafts
{

    private Map<Long, Draft> drafts = new Hashtable<Long, Draft>();

    /**
     * Simply get or create a draft for a given chapter.
     * 
     * @param chapterId
     * @return
     */
    public synchronized Draft getOrCreate(Chapter chapter)
    {
        assert chapter != null;

        Draft result;

        Long chapterId = chapter.getId();
        if (drafts.containsKey(chapterId))
        {
            result = drafts.get(chapterId);
            return result;
        }

        result = new Draft();
        result.setTimestamp(chapter.getLastModified());
        drafts.put(chapterId, result);
        return result;
    }

    /**
     * Remove draft if exists.
     * 
     * @param chapter
     * @return
     */
    public synchronized Draft remove(Chapter chapter)
    {
        assert chapter != null;

        if (drafts.containsKey(chapter.getId())) { return drafts.remove(chapter.getId()); }
        return null;
    }

}
