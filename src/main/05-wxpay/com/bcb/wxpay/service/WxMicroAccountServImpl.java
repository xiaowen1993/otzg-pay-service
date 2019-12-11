package com.bcb.wxpay.service;

import com.bcb.base.AbstractServ;
import com.bcb.util.DateUtil;
import com.bcb.util.FuncUtil;
import com.bcb.wxpay.dao.WxMicroAccountDao;
import com.bcb.wxpay.dao.WxMicroAccountLogDao;
import com.bcb.wxpay.entity.WxMicroAccount;
import com.bcb.wxpay.entity.WxMicroAccountLog;
import com.bcb.wxpay.util.service.WxMicroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * @Author G.
 * @Date 2019/11/30 0030 上午 11:25
 */
@Service
public class WxMicroAccountServImpl extends AbstractServ implements WxMicroAccountServ {
    @Autowired
    WxMicroAccountDao wxMicroAccountDao;
    @Autowired
    WxMicroAccountLogDao wxMicroAccountLogDao;

    @Override
    @Transactional
    public int saveMicroAccount(WxMicroAccount wxMicroAccount) {
        wxMicroAccount.setId(getId());
        wxMicroAccount.setBusinessCode(getBusinessCode(wxMicroAccount.getUnitId()));
        wxMicroAccount.setCreateTime(DateUtil.now());
        wxMicroAccount.setUpdateTime(DateUtil.now());
        wxMicroAccount.setStatus(0);
        wxMicroAccountDao.save(wxMicroAccount);
        //提交微信审核
        new Thread(() -> doMicroSubmit(wxMicroAccount)).start();
        return 0;
    }

    //自动生成订单号
    String getBusinessCode(String unitId) {
        return DateUtil.yearMonthDayTimeShort() + FuncUtil.getRandInt(0001, 9999) + Math.abs(unitId.hashCode());
    }

    void doMicroSubmit(WxMicroAccount wxMicroAccount) {
        String applyment_id = new WxMicroUtil().doMicroSubmit(wxMicroAccount);
        if (null != applyment_id) {
            return;
        }

        wxMicroAccount.setApplymentId(applyment_id);
        wxMicroAccountDao.save(wxMicroAccount);
    }

    @Override
    public Map query(String unitId) {
        Optional<WxMicroAccount> op = wxMicroAccountDao.findByUnitId(unitId);
        if (!op.isPresent()) {
            return null;
        }

        return microAccountQuery(op.get().getUnitId(),op.get().getBusinessCode());
    }

    //查询审批结果
    Map microAccountQuery(String unitId,String businessCode) {
        Optional<WxMicroAccountLog> op = wxMicroAccountLogDao.findByBusinessCode(businessCode);
        if (op.isPresent()) {
            return op.get().getJson();
        }

        Map map = new WxMicroUtil().doMicroSubmitQuery(businessCode);
        //{nonce_str=zciinAeKFho1agXy, applyment_state=REJECTED, sign=6D9A2C9C7B20453F16AD14DC76F3EDACE138D41E781B71BD571C4AD740A60F72,
        // applyment_state_desc=已驳回, return_msg=OK, result_code=SUCCESS,
        // audit_detail={"audit_detail":[{"param_name":"id_card_copy","reject_reason":"身份证正面识别失败，请重新上传"}]},
        // return_code=SUCCESS, applyment_id=2000002132954441}

        //创建一个结果对象
        WxMicroAccountLog wxMicroAccountLog = new WxMicroAccountLog();
        wxMicroAccountLog.setBusinessCode(businessCode);
        wxMicroAccountLog.setId(getId());

        wxMicroAccountLog.setCreateTime(DateUtil.now());

        wxMicroAccountLog.setUnitId(unitId);

        Map result = (Map) map.get("data");
        P("小微进件审批成功 businessCode=>" + businessCode);

        wxMicroAccountLog.setApplymentId(result.get("applyment_id").toString());
        wxMicroAccountLog.setApplymentState(result.get("applyment_state").toString());
        wxMicroAccountLog.setApplymentStateDesc(result.get("applyment_state_desc").toString());

        //如果失败
        if (map.get("success").equals(false)
                && null != result.get("audit_detail")){
            wxMicroAccountLog.setAuditDetail(result.get("audit_detail").toString());
            wxMicroAccountLog.setStatus(0);
        }
        //如果成功
        if (map.get("success").equals(true)
                && null != result.get("sub_mch_id")
                && null != result.get("sign_url")){
            wxMicroAccountLog.setSubMchId(result.get("sub_mch_id").toString());
            wxMicroAccountLog.setSignUrl(result.get("sign_url").toString());
            wxMicroAccountLog.setStatus(1);
        }
        //保存结果
        wxMicroAccountLogDao.save(wxMicroAccountLog);
        return wxMicroAccountLog.getJson();
    }
}
