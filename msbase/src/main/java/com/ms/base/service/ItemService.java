package com.ms.base.service;

import com.ms.base.error.BusinessException;
import com.ms.base.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    // 改进：返回ItemModel
    // 创建商品
    public ItemModel createItem(ItemModel itemModel) throws BusinessException;

    // 浏览商品详情
    public ItemModel getItemById(Integer id);

    // 查询商品列表
    public List<ItemModel> listItem();

    // 减库存
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    // 加销量
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
