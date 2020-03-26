package com.ms.base.controller;

import com.ms.base.controller.viewobject.OrderVO;
import com.ms.base.error.BusinessException;
import com.ms.base.error.EmBusinessErr;
import com.ms.base.response.CommonReturnType;
import com.ms.base.service.OrderService;
import com.ms.base.service.model.OrderModel;
import com.ms.base.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller("order")
@RequestMapping("/order")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrderController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    @CrossOrigin
    public CommonReturnType createOrder(@RequestParam(name="itemId") Integer itemId,
                                        @RequestParam(name="amount") Integer amount,
                                        @RequestParam(name = "promoId",required = false) Integer promoId) throws BusinessException {
        // 获取用户登录信息
        // boolean isLogin = (boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        // 改进：用Boolean，而不是boolean，这样可以防止没有"IS_LOGIN"时报空指针异常
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin==null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessErr.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, amount, promoId);
        if (orderModel == null){
            throw new BusinessException(EmBusinessErr.ORDER_CREATE_FAILE);
        }

        OrderVO orderVO = convertFromModel(orderModel);
        return CommonReturnType.create(orderVO);
    }


    // model --> vo
    private OrderVO convertFromModel(OrderModel orderModel){
        if (orderModel == null){
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderModel, orderVO);
        return orderVO;
    }
}
