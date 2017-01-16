package com.toranado.bestafe.adpter;

/**
 * Created by sbhar on 14-07-2016.
 */
public class ShowCartDataListItem {

    String Modelname;
    String Price;
    String MPN;
    String imagePath;
    String Product_Id;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(String product_Id) {
        Product_Id = product_Id;
    }

    public String getModelname() {
        return Modelname;
    }

    public void setModelname(String modelname) {
        Modelname = modelname;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMPN() {
        return MPN;
    }

    public void setMPN(String MPN) {
        this.MPN = MPN;
    }
}
