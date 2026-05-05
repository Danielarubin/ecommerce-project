package com.school.ecommerce.model;

import java.util.List;

public class Creator {
    private String name;
    private String location;
    private int numberOfItems;
    private List<String> tags;
    private String avatarInitial;
    private String avatarGradient; // can be null/empty
    private String mainBg;
    private String sub1Bg;
    private String sub2Bg;

    public Creator() {}

    public Creator(String name, String location, int numberOfItems, List<String> tags, 
                   String avatarInitial, String avatarGradient, String mainBg, String sub1Bg, String sub2Bg) {
        this.name = name;
        this.location = location;
        this.numberOfItems = numberOfItems;
        this.tags = tags;
        this.avatarInitial = avatarInitial;
        this.avatarGradient = avatarGradient;
        this.mainBg = mainBg;
        this.sub1Bg = sub1Bg;
        this.sub2Bg = sub2Bg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAvatarInitial() {
        return avatarInitial;
    }

    public void setAvatarInitial(String avatarInitial) {
        this.avatarInitial = avatarInitial;
    }

    public String getAvatarGradient() {
        return avatarGradient;
    }

    public void setAvatarGradient(String avatarGradient) {
        this.avatarGradient = avatarGradient;
    }

    public String getMainBg() {
        return mainBg;
    }

    public void setMainBg(String mainBg) {
        this.mainBg = mainBg;
    }

    public String getSub1Bg() {
        return sub1Bg;
    }

    public void setSub1Bg(String sub1Bg) {
        this.sub1Bg = sub1Bg;
    }

    public String getSub2Bg() {
        return sub2Bg;
    }

    public void setSub2Bg(String sub2Bg) {
        this.sub2Bg = sub2Bg;
    }
}
