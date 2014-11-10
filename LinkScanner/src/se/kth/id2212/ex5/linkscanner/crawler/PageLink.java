package se.kth.id2212.ex5.linkscanner.crawler;

import java.io.Serializable;

public class PageLink implements Serializable
{

    final String link;
    final String linkText;

    public PageLink(String link, String linkText)
    {
        this.link = link;
        this.linkText = linkText;
    }

    public String getLink()
    {
        return link;
    }

    public String getLinkText()
    {
        return linkText;
    }

    @Override
    public String toString()
    {
        return linkText;
    }
}