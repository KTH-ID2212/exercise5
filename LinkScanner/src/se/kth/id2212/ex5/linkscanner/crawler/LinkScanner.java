package se.kth.id2212.ex5.linkscanner.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkScanner
{
    public static final Pattern TAG_PATTERN = Pattern.compile("(?i)<a([^>]+)>(.+?)</a>");
    public static final Pattern LINK_PATTERN = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");

    public static List<PageLink> scan(final String html)
    {
        Matcher tagMatch = TAG_PATTERN.matcher(html);
        List<PageLink> result = new ArrayList<PageLink>();

        while (tagMatch.find())
        {
            String href = tagMatch.group(1);
            String linkText = tagMatch.group(2);
            Matcher linkMatcher = LINK_PATTERN.matcher(href);
            while (linkMatcher.find())
            {
                String link = linkMatcher.group(1);
                PageLink pageLink = new PageLink(link, linkText);
                result.add(pageLink);
            }
        }

        return result;
    }

}
