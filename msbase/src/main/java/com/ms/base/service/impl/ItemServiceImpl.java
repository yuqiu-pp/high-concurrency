package com.ms.base.service.impl;

import com.ms.base.dao.ItemDOMapper;
import com.ms.base.dao.ItemStockDOMapper;
import com.ms.base.dataobject.ItemDO;
import com.ms.base.dataobject.ItemStockDO;
import com.ms.base.error.BusinessException;
import com.ms.base.error.EmBusinessErr;
import com.ms.base.service.ItemService;
import com.ms.base.service.model.ItemModel;
import com.ms.base.service.model.PromoModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    ItemDOMapper itemDOMapper;

    @Autowired
    ItemStockDOMapper itemStockDOMapper;

    @Autowired
    PromoSerivce promoSerivce;

    // 这里不捕获异常，controller捕获，统一返回错误创建失败
    @Override
    // 改进：事务标签打的位置
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        // 改进：校验入参
        if (itemModel == null){
            return null;
        }
        // 改进：注解方式校验参数
        if (itemModel.getPrice().doubleValue()<=0
                || itemModel.getStock()<0
                || StringUtils.isEmpty(itemModel.getTitle())){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR);
        }


        // 改进：转化封装接口
        // ItemDO itemDO = new ItemDO();
        // BeanUtils.copyProperties(itemModel, itemDO);
        ItemDO itemDO = this.convertItemDOFromModel(itemModel);
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());

        // ItemStockDO itemStockDO = new ItemStockDO();
        // BeanUtils.copyProperties(itemModel,itemStockDO);
        ItemStockDO itemStockDO = this.convertItemStockFromModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        // 返回itemModel, ? 直接返回参数的itemmodel
        // 改进：
        return itemModel;//this.getItemById(itemModel.getId());
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null){
            return null;
        }
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        ItemModel itemModel = this.convertItemModel(itemDO, itemStockDO);
        // 秒杀
        PromoModel promoModel = promoSerivce.getPromoByItemId(itemModel.getId());
        // 改进：没有或者结束的秒杀活动不让用户感知
        if (promoModel!=null || promoModel.getStatus()!=3){
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;
    }

    // 如何组合List<ItemModel>
    @Override
    public List<ItemModel> listItem() {
        // 先查 itemDO列表
        List<ItemDO> itemDOList = itemDOMapper.selectItems();

        // 方法1：组合 每次查询
        // List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
        //     ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        //     ItemModel itemModel = this.convertItemModel(itemDO, itemStockDO);
        //     return itemModel;
        // }).collect(Collectors.toList());

        // 方法2：批量查询itemstockList
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            ItemModel itemModel = new ItemModel();
            BeanUtils.copyProperties(itemDO, itemModel);
            return itemModel;
        }).collect(Collectors.toList());

        // idList
        List<Integer> itemIdList = itemDOList.stream().map(itemDO -> itemDO.getId()).collect(Collectors.toList());
        // 不能为空
        if (itemIdList != null){
            List<ItemStockDO> itemStockDOList = itemStockDOMapper.selectByItemIdList(itemIdList);
            // list<Entity>  -->  map<id, Entity>
            Map<Integer,ItemStockDO> stockDOMap = itemStockDOList.stream().collect(Collectors.toMap(ItemStockDO::getItemId, Function.identity()));
            // itemModel的stock赋值
            // for (ItemModel itemModel : itemModelList){
            //     ItemStockDO itemStockDO = stockDOMap.get(itemModel.getId());
            //     itemModel.setStock(itemStockDO.getStock());
            // }
            itemModelList.forEach(itemModel -> itemModel.setStock(stockDOMap.get(itemModel.getId()).getStock()));
        }

        return itemModelList;
    }

    @Transactional
    @Override
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        // 库存是否足够通过sql来实现
        // 校验商品是否存在
        ItemStockDO itemSockDO = itemStockDOMapper.selectByItemId(itemId);
        if (itemSockDO == null){
            throw new BusinessException(EmBusinessErr.ITEM_NOT_FOUND);
        }
        int affectRow = itemStockDOMapper.decreaseStock(itemId, amount);
        if (affectRow < 0){
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        // 参数检查
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(itemId);
        if (itemDO == null){
            throw new BusinessException(EmBusinessErr.ITEM_NOT_FOUND);
        }
        // itemDO.setSales(itemDO.getSales() + amount);
        // int affectRow = itemDOMapper.updateByPrimaryKeySelective(itemDO);
        // 改进：这样更新可能影响itemDO其它字段的值，例如并发情况下发生覆盖
        itemDOMapper.increateSales(itemId, amount);
    }


    private ItemModel convertItemModel(ItemDO itemDO, ItemStockDO itemStockDO){
        if (itemDO == null){
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        // 改进：price传给前端要用bigdecimal，浮点的精度问题
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }


    private ItemDO convertItemDOFromModel(ItemModel itemModel){
        // 改进：入参校验
        if (itemModel == null){
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);

        // 改进：price的类型要用BigDecimal，double传给前端有精度问题
        //      BeanUtils拷贝时，类型不一致的不会拷贝，所以price要单独赋值
        itemDO.setPrice(itemModel.getPrice().doubleValue());

        return itemDO;
    }

    private ItemStockDO convertItemStockFromModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

}
