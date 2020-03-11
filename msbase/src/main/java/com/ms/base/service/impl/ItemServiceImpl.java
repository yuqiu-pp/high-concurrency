package com.ms.base.service.impl;

import com.ms.base.dao.ItemDOMapper;
import com.ms.base.dao.ItemStockDOMapper;
import com.ms.base.dataobject.ItemDO;
import com.ms.base.dataobject.ItemStockDO;
import com.ms.base.error.BusinessException;
import com.ms.base.error.EmBusinessErr;
import com.ms.base.service.ItemService;
import com.ms.base.service.model.ItemModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
