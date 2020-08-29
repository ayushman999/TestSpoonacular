package com.example.android.testspoonacular;

import java.util.ArrayList;

public class Elements {
    private String food_img_url;
    private String title;
    private ArrayList<String> missing_list;
    private ArrayList<String> available_list;
    public Elements(String food_img_url, String title, ArrayList<String> missing_list, ArrayList<String> available_list) {
        this.food_img_url = food_img_url;
        this.title = title;
        this.missing_list = missing_list;
        this.available_list = available_list;
    }

    public String getFood_img_url() {
        return food_img_url;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getMissing_list() {
        return missing_list;
    }

    public ArrayList<String> getAvailable_list() {
        return available_list;
    }
}
