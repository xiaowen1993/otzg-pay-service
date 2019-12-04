package com.bcb.wxpay.api;

import com.bcb.pay.api.AbstractController;
import com.bcb.util.CheckUtil;
import com.bcb.util.RespTips;
import com.bcb.wxpay.dto.WxMicroAccountDto;
import com.bcb.wxpay.entity.WxMicroAccount;
import com.bcb.wxpay.service.MicroAccountServ;
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
    MicroAccountServ microAccountServ;
    @RequestMapping("/pay/microAccount/save")
	public final void save(String unitId,WxMicroAccountDto wxMicroAccountDto){
        if(CheckUtil.isEmpty(unitId)){
            sendParamError();
            return;
        }

        if(null!=microAccountServ.query(unitId)){
            sendJson(false, RespTips.PAYACCOUNT_FOUND.code,"商户材料已提交");
            return;
        }

        wxMicroAccountDto.setIdCardValidTime("[\"1970-01-01\",\"长期\"]");
        //校验
        WxMicroAccountUtil check = new WxMicroAccountUtil(wxMicroAccountDto);
        if(!check.isPass()){
            sendJson(check.getMsg());
            return;
        }

        WxMicroAccount wxMicroAccount = check.getPojo();
        wxMicroAccount.setUnitId(unitId);

        int r = microAccountServ.saveMicroAccount(wxMicroAccount);
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

        Map jo =microAccountServ.query(unitId);
        if(null == jo){
            sendFail();
        }else {
            sendSuccess(jo);
        }
    }

}
