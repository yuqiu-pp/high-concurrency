package com.ms.base.dataobject;

import java.util.Date;

public class OrderDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_record.id
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_record.user_id
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_record.item_id
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    private Integer itemId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_record.amount
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    private Integer amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_record.item_price
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    private Double itemPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_record.order_price
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    private Double orderPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_record.create_time
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    private Date createTime;

    private Integer promoId;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_record.id
     *
     * @return the value of order_record.id
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_record.id
     *
     * @param id the value for order_record.id
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_record.user_id
     *
     * @return the value of order_record.user_id
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_record.user_id
     *
     * @param userId the value for order_record.user_id
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_record.item_id
     *
     * @return the value of order_record.item_id
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_record.item_id
     *
     * @param itemId the value for order_record.item_id
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_record.amount
     *
     * @return the value of order_record.amount
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_record.amount
     *
     * @param amount the value for order_record.amount
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_record.item_price
     *
     * @return the value of order_record.item_price
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public Double getItemPrice() {
        return itemPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_record.item_price
     *
     * @param itemPrice the value for order_record.item_price
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_record.order_price
     *
     * @return the value of order_record.order_price
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public Double getOrderPrice() {
        return orderPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_record.order_price
     *
     * @param orderPrice the value for order_record.order_price
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_record.create_time
     *
     * @return the value of order_record.create_time
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_record.create_time
     *
     * @param createTime the value for order_record.create_time
     *
     * @mbg.generated Wed Mar 18 21:31:12 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}