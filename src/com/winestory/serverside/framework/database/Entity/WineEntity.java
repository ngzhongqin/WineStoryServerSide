package com.winestory.serverside.framework.database.Entity;

import javax.persistence.*;

/**
 * Created by zhongqinng on 28/7/15.
 * WineEntity
 */
@Entity
@Table(name = "wine", schema = "winestory", catalog = "winestory")
public class WineEntity {
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
    private double price;


    @Id
    @SequenceGenerator(name="wine_id_seq",
            sequenceName="wine_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="wine_id_seq")
    @Column(name = "id", updatable=false)
    private Long id;

    public Long getId(){return this.id;}
    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "tasting_note")
    public String getTasting_note() {
        return tasting_note;
    }

    public void setTasting_note(String tasting_note) {
        this.tasting_note = tasting_note;
    }

    @Basic
    @Column(name = "year")
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    @Basic
    @Column(name = "colour")
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
    @Basic
    @Column(name = "nose")
    public String getNose() {
        return nose;
    }

    public void setNose(String nose) {
        this.nose = nose;
    }
    @Basic
    @Column(name = "palate")
    public String getPalate() {
        return palate;
    }

    public void setPalate(String palate) {
        this.palate = palate;
    }
    @Basic
    @Column(name = "grapes")
    public String getGrapes() {
        return grapes;
    }

    public void setGrapes(String grapes) {
        this.grapes = grapes;
    }
    @Basic
    @Column(name = "volume")
    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
    @Basic
    @Column(name = "available_stock")
    public int getAvailable_stock() {
        return available_stock;
    }

    public void setAvailable_stock(int available_stock) {
        this.available_stock = available_stock;
    }
    @Basic
    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    @Basic
    @Column(name = "image_path")
    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
