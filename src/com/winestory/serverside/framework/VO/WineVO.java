package com.winestory.serverside.framework.VO;

/**
 * Created by zhongqinng on 28/7/15.
 * WineVO
 */
public class WineVO {
    private Long id;
    private String name;
    private String image_path;
    private String tasting_note;
    private String year;
    private String colour;
    private String nose;
    private String palate;
    private String grapes;
    private int volume;
    private int available_stock;
    private Number price;

    public WineVO(Long id,
            String name,
            String image_path,
            String tasting_note,
            String year,
            String colour,
            String nose,
            String palate,
            String grapes,
            int volume,
            int available_stock,
            Number price){
        this.id = id;
        this.name=name;
        this.image_path=image_path;
        this.tasting_note=tasting_note;
        this.year=year;
        this.colour=colour;
        this.nose=nose;
        this.palate=palate;
        this.grapes=grapes;
        this.volume=volume;
        this.available_stock=available_stock;
        this.price=price;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getTasting_note() {
        return tasting_note;
    }

    public void setTasting_note(String tasting_note) {
        this.tasting_note = tasting_note;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getNose() {
        return nose;
    }

    public void setNose(String nose) {
        this.nose = nose;
    }

    public String getPalate() {
        return palate;
    }

    public void setPalate(String palate) {
        this.palate = palate;
    }

    public String getGrapes() {
        return grapes;
    }

    public void setGrapes(String grapes) {
        this.grapes = grapes;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getAvailable_stock() {
        return available_stock;
    }

    public void setAvailable_stock(int available_stock) {
        this.available_stock = available_stock;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }
}
