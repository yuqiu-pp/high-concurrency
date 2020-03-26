package com.ms.base.service.model;

public enum EmPromoStatus {
    EM_PROMO_NOT_START(1),
    EM_PROMO_IS_START(2),
    EM_PROMO_IS_END(3),
    ;


    private Integer status;
    EmPromoStatus(Integer n){
        this.status = n;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
