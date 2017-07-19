package com.venkibellu.myapplication;

/**
 * Created by captaindavinci on 20/7/17.
 */

public class News {
    private String name, time, details, image, organisation;

    News(String name, String time, String details, String image, String organisation) {
        this.name = name;
        this.time = time;
        this.details = details;
        this.image = image;
        this.organisation = organisation;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDetails() {
        return details;
    }

    public String getImage() {
        return image;
    }

    public String getOrganisation() {
        return organisation;
    }
}
