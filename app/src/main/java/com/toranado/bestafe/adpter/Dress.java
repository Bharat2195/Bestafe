package com.toranado.bestafe.adpter;

/**
 * Created by sbhar on 30-06-2016.
 */
public class Dress {
    private String name;
    private String numOfSongs;
    //private int thumbnail;
    private String imagePath;
    private String productId;

    public Dress() {

    }


    public Dress(String name, String numOfSongs, String imagePath, String productId) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.imagePath = imagePath;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(String numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    /*public int getThumbnail() {
        return thumbnail;
    }*/

    /*public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }*/
}
