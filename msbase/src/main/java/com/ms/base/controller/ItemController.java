package com.ms.base.controller;

import com.ms.base.controller.viewobject.ItemVO;
import com.ms.base.error.BusinessException;
import com.ms.base.error.EmBusinessErr;
import com.ms.base.response.CommonReturnType;
import com.ms.base.service.impl.ItemServiceImpl;
import com.ms.base.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ms.base.controller.BaseController.CONTENT_TYPE_FORMED;


@Controller("item")
@RequestMapping("/item")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class ItemController extends BaseController{

    @Autowired
    ItemServiceImpl itemService;

    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    // 应该不需要跨域
    @CrossOrigin
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                           @RequestParam(name = "description") String description,
                           @RequestParam(name = "stock") Integer stock,
                           @RequestParam(name = "price")BigDecimal price,
                           @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        // 检查用户权限

        // 参数检查service层

        // 组合itemmodel
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setStock(stock);
        itemModel.setPrice(price);
        itemModel.setImgUrl(imgUrl);
        itemModel.setSales(0);

        // 调用service接口
        ItemModel itemModel1 = itemService.createItem(itemModel);

        // 返回ItemVO
        ItemVO itemVO = this.convertFromItemModel(itemModel);

        // 改进：返回的统一格式给忘了
        // return itemVO;
        return CommonReturnType.create(itemVO);
    }


    @RequestMapping(value = "list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType list(){
        List<ItemModel> itemModelList = itemService.listItem();
        // dataobject -->  vo
        List<ItemVO> itemVOList = convertFromItemModelList(itemModelList);

        return CommonReturnType.create(itemVOList);
    }


    @RequestMapping(value = "get", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType get(@RequestParam(name = "id") Integer id) throws BusinessException {
        ItemModel itemModel = itemService.getItemById(id);
        if (itemModel == null){
            throw new BusinessException(EmBusinessErr.ITEM_NOT_FOUND);
        }
        ItemVO itemVO = this.convertFromItemModel(itemModel);
        // itemService层控制，有秒杀活动，controller才能不会null
        if (itemModel.getPromoModel() != null){
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPrice(itemModel.getPromoModel().getItemPrice());
            itemVO.setPromoId(itemModel.getId());
        }else{
            itemVO.setPromoStatus(0);
        }
        return CommonReturnType.create(itemVO);
    }


    private List<ItemVO> convertFromItemModelList(List<ItemModel> itemModelList){
        List<ItemVO> itemVOList = new ArrayList<>();

        // for (ItemModel itemModel : itemModelList){
        //     itemVOList.add(convertFromItemModel(itemModel));
        // }

        // stream API / lambda表达式
        itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = this.convertFromItemModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        // lambda表达式只有一行，且没有花括号，可以省略return
        // itemVOList = itemModelList.stream().map(
        //         m -> convertFromItemModel(m))
        //         .collect(Collectors.toList());
        //
        // itemVOList = itemModelList.stream().map(this::convertFromItemModel).collect(Collectors.toList());

        return itemVOList;
    }


    private ItemVO convertFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        return itemVO;
    }
}
