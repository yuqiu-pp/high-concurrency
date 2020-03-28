package com.ms.base.service.impl;

import com.ms.base.dao.OrderDOMapper;
import com.ms.base.dao.SequenceDOMapper;
import com.ms.base.dataobject.OrderDO;
import com.ms.base.dataobject.SequenceDO;
import com.ms.base.error.BusinessException;
import com.ms.base.error.EmBusinessErr;
import com.ms.base.service.OrderService;
import com.ms.base.service.model.ItemModel;
import com.ms.base.service.model.OrderModel;
import com.ms.base.service.model.PromoModel;
import com.ms.base.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.element.NestingKind;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserServiceImpl userService;


    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount, Integer promoId) throws BusinessException {
        // 参数校验
        if (amount == 0){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR);
        }

        // 校验商品信息  stock>amount
        // ItemDO itemDO = itemDOMapper.selectByPrimaryKey(ItemId);
        // ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(ItemId);
        // if (itemStockDO.getStock() < 0){
        //     throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR, "商品库存不足");
        // }
        // 改进点：直接用itemService读取itemModel就行
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR, "商品部存在");
        }
        if (amount<=0 || amount > 100){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR, "购买数量不合法");
        }
        if (itemModel.getStock() < 0){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR, "商品库存不足");
        }

        // 校验活动
        if (promoId != null){
            //  活动是否存在对应的商品
            if (promoId != itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessErr.PROMO_ITEM_NOT_FOUND);
            }else {
                // 活动是否有效
                if (itemModel.getPromoModel().getStatus() != 2){
                    throw new BusinessException(EmBusinessErr.PROMO_VALIDATION_ERROR);
                }
            }
        }

        // 查询用户登录状态 ? 服务层没有登录状态，应该在controller层验证
        // 这里可以交易用户是否存在
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR, "用户不存在");
        }

        // 减库是存  ? 用 service，还是DO  DO轻量
        // ItemStockDO itemStockDO = new ItemStockDO();
        // itemStockDO.setItemId(itemModel.getId());
        // itemStockDO.setStock(itemModel.getStock() - 1);
        // itemStockDOMapper.updateByItemId(itemModel.getId());
        // 改进：用service接口，层次概念上的清晰
        // 落单减库存的策略
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result){
            throw new BusinessException(EmBusinessErr.ORDER_CREATE_FAILE, "库存不足");
        }

        // 订单入库
        // OrderDO orderDO = new OrderDO();
        String orderId = generateOrderNo();
        // orderDO.setId(orderId);
        // orderDO.setAmount(amount);
        // orderDO.setItemId(itemId);
        // orderDO.setOrderPrice(itemModel.getPrice().multiply(BigDecimal.valueOf(amount)).doubleValue());
        // orderDO.setItemPrice(itemModel.getPrice().doubleValue());
        // orderDO.setUserId(userId);
        // 改进；这里还是model的概念，真正写库时才对应DO
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setPromoId(promoId);
        if (promoId != null){
            orderModel.setItemPrice(itemModel.getPromoModel().getItemPrice());
        }
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setAmount(amount);
        // orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        // 改进：bigdecimal和double的计算
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(BigDecimal.valueOf(amount)));
        orderModel.setId(orderId);
        // 写数据库
        OrderDO orderDO = convertFromModel(orderModel);
        try {
            orderDOMapper.insertSelective(orderDO);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EmBusinessErr.ORDER_ID_DUPLICATE);
        } catch (Exception e){
            throw new BusinessException(EmBusinessErr.ORDER_CREATE_FAILE);
        }

        // 增加销量
        itemService.increaseSales(itemId, amount);

        // 读取orderModel返回
        // orderDO = orderDOMapper.selectByPrimaryKey(orderDO.getId());
        // orderModel = convertFromDO(orderDO);
        // 改进：直接返回orderModel，而不再去读数据库。这里与userModer
        //      这里orderModel的id不是数据库自增，不需要再次读orderDO获得
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    // 改进：1、同一个类中Propagation.REQUIRES_NEW不会生效；
    //      2、private接口上也不会生效；
    //      原因：需要通过代理对象调用才能生效，有标签虽然会生成代理对象，但上面的两种情况都是通过this调用的，而不是代理
    private String generateOrderNo(){
        StringBuilder stringBuilder = new StringBuilder();
        //  8位时间
        LocalDateTime now = LocalDateTime.now();
        stringBuilder.append(now.format(DateTimeFormatter.ISO_DATE).replace("-",""));
        // 6位自增
        SequenceDO sequenceDO = sequenceDOMapper.selectByName("order_record");
        int sequence = 0;
        sequence = sequenceDO.getCurrentValue();
        // sequenceDOMapper.updateByName("order_record", sequenceDO.getStep());
        // 改进：不需要单独写update
        sequenceDO.setCurrentValue(sequence + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6-sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        // 2位分库分表
        stringBuilder.append("00");

        return stringBuilder.toString();
    }
    // DO -> model
    private OrderModel convertFromDO(OrderDO orderDO){
        if (orderDO == null){
            return null;
        }

        OrderModel orderModel = new OrderModel();
        BeanUtils.copyProperties(orderDO, orderModel);
        return orderModel;
    }


    // model -> DO
    private OrderDO convertFromModel(OrderModel orderModel){
        if (orderModel == null){
            return null;
        }

        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
