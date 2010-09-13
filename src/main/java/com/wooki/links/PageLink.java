package com.wooki.links;

/**
 * Defines a link dedicated to page.
 * 
 * @author ccordenier
 */
public interface PageLink extends Link
{

    /**
     * Returns the page targeted by the link.
     * 
     * @return
     */
    String getPage();

    /**
     * Check if page link is currently selected by the user, can be used in menu for exemple.
     * 
     * @return
     */
    boolean isSelected();

    /**
     * Call this method to marke an item as selected.
     * 
     * @param selected
     */
    void setSelected(boolean selected);
}
