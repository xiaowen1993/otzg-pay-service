package com.bcb.wxpay.api;

import com.bcb.pay.api.AbstractController;
import com.bcb.util.CheckUtil;
import com.bcb.util.RespTips;
import com.bcb.wxpay.dto.WxMicroAccountDto;
import com.bcb.wxpay.entity.WxMicroAccount;
import com.bcb.wxpay.service.WxMicroAccountServ;
import com.bcb.wxpay.util.WxMicroAccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author G.
 * @Date 2019/11/30 0030 下午 3:10
 */
@RestController
public class MicroAccountApi extends AbstractController {
    @Autowired
    WxMicroAccountServ wxMicroAccountServ;
    @RequestMapping("/pay/microAccount/save")
	public final void save(String unitId,WxMicroAccountDto wxMicroAccountDto){
        if(CheckUtil.isEmpty(unitId)){
            sendParamError();
            return;
        }

        if(null!= wxMicroAccountServ.query(unitId)){
            sendJson(false, RespTips.PAYACCOUNT_FOUND.code,"商户材料已提交");
            return;
        }

        wxMicroAccountDto.setIdCardValidTime("[\""+wxMicroAccountDto.getIdCardValidTime()+"\",\"长期\"]");
        //校验
        WxMicroAccountUtil check = new WxMicroAccountUtil(wxMicroAccountDto);
        if(!check.isPass()){
            sendJson(check.getMsg());
            return;
        }

        //校验通过
        WxMicroAccount wxMicroAccount = check.getPojo();
        wxMicroAccount.setUnitId(unitId);

        int r = wxMicroAccountServ.saveMicroAccount(wxMicroAccount);
        if(r==0){
            sendSuccess();
        }
    }

    @RequestMapping("/pay/microAccount/query")
    public final void query(String unitId) {
        if (CheckUtil.isEmpty(unitId)) {
            sendParamError();
            return;
        }

        Map jo = wxMicroAccountServ.query(unitId);
        if(null == jo){
            sendFail();
        }else {
            sendSuccess(jo);
        }
    }

}
