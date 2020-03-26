package com.ms.base.service.model;


import java.math.BigDecimal;
import java.util.Date;

// 每个订单
public class OrderModel {

    // 一串有意义的数字
    private String id;

    // 商品相关
    private int itemId;
    // 如果promoId不为0，这里的价格是秒杀价格
    private BigDecimal itemPrice; // 单价

    private Integer amount;

    // 如果promoId不为0，这里的价格是秒杀价格
    private BigDecimal orderPrice;

    // 用户相关
    private int userId;

    private Date createTime;

    private Integer promoId;


    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
