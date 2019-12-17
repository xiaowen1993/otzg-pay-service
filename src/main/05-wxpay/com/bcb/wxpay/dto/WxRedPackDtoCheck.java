package com.bcb.wxpay.dto;

import com.bcb.util.CheckDtoUtil;
import com.bcb.util.RespTips;

/**
 * @Author G.
 * @Date 2019/12/13 0013 上午 11:25
 */
public class WxRedPackDtoCheck extends CheckDtoUtil<WxRedPackDto> {

    public WxRedPackDtoCheck(WxRedPackDto wxRedPackDto) {
        super(wxRedPackDto);
    }

    @Override
    protected void check() {
        if (checkParam(t.getRedPackOrderNo(), 28)) {
            msg = "业务单号(redPackOrderNo)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
        if (checkParam(t.getSendName(), 16)) {
            msg = "商户名称(sendName)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }

        if (checkParam(t.getReOpenId(), 28)) {
            msg = "接收人openId(reOpenId)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }

        if (checkParam(t.getWishing(), 16)) {
            msg = "红包祝福语(wishing)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }

        if (checkEmpty(t.getTotalNum())) {
            msg = "红包发送数量(totalNum)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }

        if (checkAmount(t.getTotalAmount())) {
            msg = "红包发送金额(totalAmount)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }

        if(t.getTotalNum().equals(1)
                && checkParam(t.getClientIp(),15)){
            msg = "终端ip地址(clientIp)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }

        if(checkParam(t.getActName(),32)){
            msg = "活动名称(actName)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
        if(checkParam(t.getRemark(),256)){
            msg = "备注信息(remark)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
        if(checkParam(t.getUnitId(),32)){
            msg = "子商户id(unitId)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
        if(checkParam(t.getMemberId(),32)){
            msg = "发起人id(memberId)参数必填";
            code = RespTips.PARAM_ERROR.code;
            pass = false;
            return;
        }
    }
}
