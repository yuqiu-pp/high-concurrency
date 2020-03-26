package com.ms.base.service.impl;

import com.ms.base.dao.PromoDOMapper;
import com.ms.base.dataobject.PromoDO;
import com.ms.base.error.BusinessException;
import com.ms.base.error.EmBusinessErr;
import com.ms.base.service.PromoService;
import com.ms.base.service.model.EmPromoStatus;
import com.ms.base.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoSerivce implements PromoService{

    @Autowired
    PromoDOMapper promoDOMapper;

    // 秒杀的活动信息需要在item页面显示出来，所以将其聚合给itemModel
    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
        // 改进：这里不能抛出异常，因为商品没有秒杀活动很正常
        // if (promoDO == null){
        //     throw new BusinessException(EmBusinessErr.PROMO_ITEM_NOT_FOUND);
        // }

        PromoModel promoModel = this.convertFromDO(promoDO);
        if (promoModel == null){
            return null;
        }
        // 检验秒杀状态
        if (promoModel.getStartTime().isAfterNow()){
            // promoModel.setStatus(EmPromoStatus.EM_PROMO_NOT_START); // 未开始
            promoModel.setStatus(1); // 未开始
        }
        if (promoModel.getStartTime().isBeforeNow() && promoModel.getEndTime().isAfterNow()){
            promoModel.setStatus(2); // 进行中
        }
        if (promoModel.getEndTime().isBeforeNow()){
            promoModel.setStatus(3);
        }

        return promoModel;
    }

    // dataobject -> model
    private PromoModel convertFromDO(PromoDO promoDO){
        if (promoDO == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        // 改进：价格和时间类型不同，需要手工赋值
        promoModel.setItemPrice(BigDecimal.valueOf(promoDO.getPromoItemPrice()));
        promoModel.setStartTime(new DateTime(promoDO.getStartDate()));
        promoModel.setEndTime(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
