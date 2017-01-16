package com.toranado.bestafe.adpter;

/**
 * Created by Bharat on 18/07/16.
 */
public class Album {
    private String name;
    private String numOfSongs;
    //private int thumbnail;
    private String imagePath;
    private String productId;

    public Album() {
    }

    /**
     * 
     * @param name
     * @param numOfSongs
     * @param imagePath
     * @param productId
     */
    public Album(String name, String numOfSongs,String imagePath,String productId) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.imagePath=imagePath;
        this.productId=productId;
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
