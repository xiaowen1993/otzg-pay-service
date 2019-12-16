package com.bcb.wxpay.api;

import com.bcb.base.Finder;
import com.bcb.pay.api.AbstractController;
import com.bcb.pay.entity.PayAccount;
import com.bcb.pay.service.PayAccountServ;
import com.bcb.util.CheckUtil;
import com.bcb.util.RespTips;
import com.bcb.wxpay.dto.WxRedPackDto;
import com.bcb.wxpay.dto.WxRedPackDtoCheck;
import com.bcb.wxpay.service.RedPackServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author G.
 * @Date 2019/12/13 0013 下午 2:35
 */
@RestController
public class RedPackApi extends AbstractController {
    @Autowired
    private PayAccountServ payAccountServ;
    @Autowired
    RedPackServ redPackServ;

    @RequestMapping("/wxpay/redpack/send")
    public void sendRedPack(WxRedPackDto wxRedPackDto){
        if(CheckUtil.isEmpty(wxRedPackDto)){
            sendParamError();
            return;
        }

        //校验参数
        WxRedPackDtoCheck wxRedPackDtoCheck = new WxRedPackDtoCheck(wxRedPackDto);
        wxRedPackDto = wxRedPackDtoCheck.get();
        if(null == wxRedPackDto){
            sendJson(wxRedPackDtoCheck.getMsg());
            return;
        }


        //判断基本账户是否已创建
        PayAccount payAccount = payAccountServ.findByUnitId(wxRedPackDto.getUnitId());
        if (null == payAccount
                || !payAccount.isUseable()) {
            sendJson(false, RespTips.PAYACCOUNT_IS_UNAVAILABLE.code, RespTips.PAYACCOUNT_IS_UNAVAILABLE.tips);
            return;
        }

        //立即发红包
        int r = redPackServ.sendRedPack(payAccount.getId(),wxRedPackDto);
        if(r==0){
            sendSuccess();
        }else if(r==1){
            sendJson(false, RespTips.PAYCHANNEL_SET_ERROR.code, RespTips.PAYCHANNEL_SET_ERROR.tips);
        }else if(r==2){
            sendJson(false, RespTips.PAYACCOUNT_IS_INSUFFICIENT.code, RespTips.PAYACCOUNT_IS_INSUFFICIENT.tips);
        }else{
            sendFail();
        }
    }

    @RequestMapping("/wxpay/redpack/find")
    public void find(String unitId, Finder f){
        if(CheckUtil.isEmpty(unitId)){
            sendParamError();
            return;
        }
        sendJson(redPackServ.findWxRedPackOrderByUnit(f,unitId));
    }

    @RequestMapping("/wxpay/redpack/query")
    public void sendRedPack(String redPackOrderNo){
        if(CheckUtil.isEmpty(redPackOrderNo)){
            sendParamError();
            return;
        }
        sendJson(redPackServ.query(redPackOrderNo));
    }
}
