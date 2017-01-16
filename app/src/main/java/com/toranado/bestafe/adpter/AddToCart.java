package com.toranado.bestafe.adpter;

import java.util.ArrayList;

/**
 * Created by sbhar on 11-07-2016.
 */
public class AddToCart {

    ArrayList<String>listTitle=new ArrayList<String>();
    ArrayList<String>listCount=new ArrayList<String>();
    ArrayList<String>listPrice=new ArrayList<String>();
    ArrayList<String>listImagePath=new ArrayList<String>();
    ArrayList<String>listProductId=new ArrayList<String>();

    public ArrayList<String> getListProductId() {
        return listProductId;
    }

    public void setListProductId(ArrayList<String> listProductId) {
        this.listProductId = listProductId;
    }

    public ArrayList<String> getListTitle() {
        return listTitle;
    }

    public void setListTitle(ArrayList<String> listTitle) {
        this.listTitle = listTitle;
    }

    public ArrayList<String> getListCount() {
        return listCount;
    }

    public void setListCount(ArrayList<String> listCount) {
        this.listCount = listCount;
    }

    public ArrayList<String> getListPrice() {
        return listPrice;
    }

    public void setListPrice(ArrayList<String> listPrice) {
        this.listPrice = listPrice;
    }

    public ArrayList<String> getListImagePath() {
        return listImagePath;
    }

    public void setListImagePath(ArrayList<String> listImagePath) {
        this.listImagePath = listImagePath;
    }
}
