package com.jcleary.beans;

/**
 * Encapsulates a single reddit post, containing it's karma and name.
 */
public class RedditPost {

    private String karma;
    private String name;
    private int numberOfComments;

    public RedditPost(String karma, String name, int numberOfComments) {
        this.karma = karma;
        this.name = name;
        this.numberOfComments = numberOfComments;
    }

    public String getKarma() {
        return karma;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    @Override
    public String toString() {
        return "RedditPost{" +
                "karma='" + karma + '\'' +
                ", name='" + name + '\'' +
                ", numberOfComments=" + numberOfComments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedditPost that = (RedditPost) o;

        if (getNumberOfComments() != that.getNumberOfComments()) return false;
        if (getKarma() != null ? !getKarma().equals(that.getKarma()) : that.getKarma() != null) return false;
        return !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null);

    }

    @Override
    public int hashCode() {
        int result = getKarma() != null ? getKarma().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getNumberOfComments();
        return result;
    }
}
