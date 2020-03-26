package com.ms.base.controller.viewobject;

import org.joda.time.DateTime;

import java.math.BigDecimal;


public class ItemVO {
    private Integer id;
    // 商品名称
    private String title;
    // 库存
    private Integer stock;
    // 销量
    private Integer sales;
    // 商品描述
    private String description;
    // 商品图片
    private String imgUrl;
    // 商品价格
    private BigDecimal price;

    // 秒杀状态
    private Integer promoStatus;
    private DateTime promoStartTime;
    private BigDecimal promoPrice;
    private Integer promoId;

    public Integer getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }

    public DateTime getPromoStartTime() {
        return promoStartTime;
    }

    public void setPromoStartTime(DateTime promoStartTime) {
        this.promoStartTime = promoStartTime;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
