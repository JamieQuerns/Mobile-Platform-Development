/**
 Name: James Querns
 Matriculation Number: S2026518
 */

package com.example.trafficscotland;

public class Incedent {
    private String title;
    private String description;
    private String link;
    private String georssPoint;
    private String author;
    private String comment;
    private String pubDate;

    public Incedent()
    {
        title = "";
        description = "";
        link = "";
        georssPoint = "";
        author = "";
        comment = "";
        pubDate = "";
    }

    public Incedent(String atitle, String adescription, String alink, String ageorssPoint, String aauthor, String acomment, String apubDate)
    {
        title = atitle;
        description = adescription;
        link = alink;
        georssPoint = ageorssPoint;
        author = aauthor;
        comment = acomment;
        pubDate = apubDate;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public String getGeorssPoint() {
        return georssPoint;
    }
    public void setGeorssPoint(String geoPoint) {
        this.georssPoint = geoPoint;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString()
    {
        String temp = title  + "\n" + "\n" +
                description + " \n" +
                link + " \n" + "\n" +
                georssPoint + "\n" +
                author + " \n" +
                comment + " \n" +
                pubDate+ " \n" + " \n" +
                "―――――――――――――――――――――"+ " \n";
        return temp;
    }
}
