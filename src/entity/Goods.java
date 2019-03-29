package entity;

public class Goods {
    private int goodsId;
    private String goodsName;
    private double price;
    private String userId;
    private int quantity;
    private int initNum;
    private String picAddress1;
    private String picAddress2;
    private String picAddress3;


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getInitNum() {
        return initNum;
    }

    public void setInitNum(int initNum) {
        this.initNum = initNum;
    }

    public String getPicAddress1() {
        return picAddress1;
    }

    public void setPicAddress1(String picAddress1) {
        this.picAddress1 = picAddress1;
    }

    public String getPicAddress2() {
        return picAddress2;
    }

    public void setPicAddress2(String picAddress2) {
        this.picAddress2 = picAddress2;
    }

    public String getPicAddress3() {
        return picAddress3;
    }

    public void setPicAddress3(String picAddress3) {
        this.picAddress3 = picAddress3;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
}
