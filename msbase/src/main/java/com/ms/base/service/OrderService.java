package com.ms.base.service;

import com.ms.base.error.BusinessException;
import com.ms.base.service.model.OrderModel;

public interface OrderService {

    public OrderModel createOrder(Integer userId, Integer ItemId, Integer amount, Integer promoId) throws BusinessException;


}
