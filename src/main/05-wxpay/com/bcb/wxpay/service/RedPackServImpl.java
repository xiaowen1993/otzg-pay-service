package com.bcb.wxpay.service;

import com.alibaba.fastjson.JSONObject;
import com.bcb.base.AbstractServ;
import com.bcb.base.Finder;
import com.bcb.base.Page;
import com.bcb.pay.entity.PayChannelAccount;
import com.bcb.pay.entity.PayOrder;
import com.bcb.pay.service.PayChannelAccountServ;
import com.bcb.util.*;
import com.bcb.wxpay.dao.WxRedPackDao;
import com.bcb.wxpay.dao.WxRedPackLogDao;
import com.bcb.wxpay.dto.WxRedPackDto;
import com.bcb.wxpay.entity.WxRedPack;
import com.bcb.wxpay.entity.WxRedPackLog;
import com.bcb.wxpay.util.service.WxRedPackUtil;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * @Author G.
 * @Date 2019/12/13 0013 上午 10:41
 */
@Service
public class RedPackServImpl extends AbstractServ implements RedPackServ {
    @Autowired
    WxRedPackDao wxRedPackDao;

    @Autowired
    WxRedPackLogDao wxRedPackLogDao;
    @Autowired
    private PayChannelAccountServ payChannelAccountServ;

    @Override
    @Transactional
    public int sendRedPack(Long payAccountId,WxRedPackDto wxRedPackDto) {
        Optional<WxRedPack> op = wxRedPackDao.findByRedPackOrderNo(wxRedPackDto.getRedpackOrderNo());
        //微信发红包是可重入接口
        if(!op.isPresent()){
            return sendFirst(payAccountId,wxRedPackDto);
        }

        //如果已经失败
        if(op.get().getStatus().equals(-1)){
            return -1;
        }

        //如果已经发送{-1:发送失败,0:发送中,1:已发送,2:已接收,3:已退款}
        if(op.get().getStatus()>1){
            return 0;
        }

        //需要再次查询确定
        new WxRedPackUtil().pay(op.get());
        return query(op.get().getRedPackOrderNo());
    }

    int sendFirst(Long payAccountId,WxRedPackDto wxRedPackDto){
        //获取支付渠道商户号
        PayChannelAccount payChannelAccount = payChannelAccountServ.findByAccountAndPayChannel(payAccountId, "wxpay");
        if (null == payChannelAccount
                || null == payChannelAccount.getPayChannelAccount()) {
            return 1;
        }

        //发红包金额
        BigDecimal amount = FuncUtil.getBigDecimalScale(new BigDecimal(wxRedPackDto.getTotalAmount()));
        PT("发红包金额="+amount);


        //如果微信账户余额不足返回
        if(payChannelAccount.getBalance().compareTo(amount)<0){
            return 2;
        }


        RLock lock = redisson.getLock(""+payChannelAccount.getId());
        try {
            boolean l = lock.tryLock(30, TimeUnit.SECONDS);
            if (!l) {
                P("redisson lock false");
                return 1;
            }

            String payRedPackOrderNo = getPayOrderNo(wxRedPackDto.getUnitId());

            WxRedPack wxRedPack = new WxRedPack();
            wxRedPack.setId(getId());
            wxRedPack.setSubMchId(payChannelAccount.getPayChannelAccount());
            wxRedPack.setPayRedPackOrderNo(payRedPackOrderNo);
            wxRedPack.setUnitId(wxRedPackDto.getUnitId());
            wxRedPack.setMemberId(wxRedPackDto.getMemberId());
            wxRedPack.setCreateTime(DateUtil.now());
            wxRedPack.setUpdateTime(DateUtil.now());
            wxRedPack.setStatus(WxRedPack.StatusType.WAIT.index);

            trans(wxRedPack,wxRedPackDto);
            wxRedPackDao.save(wxRedPack);

            //冻结支付渠道账户金额
            payChannelAccountServ.freezeBalance(payChannelAccount,amount);

            int r = new WxRedPackUtil().pay(wxRedPack);
            if(r==2){
                new WxRedPackUtil().pay(wxRedPack);
            }

            return query(wxRedPack.getRedPackOrderNo());

        }catch (Exception e){
            e.printStackTrace();
            return 3;
        }finally {
            lock.unlock();
        }
    }


    void trans(WxRedPack wxRedPack, WxRedPackDto wxRedPackDto) {
        wxRedPack.setActName(wxRedPackDto.getActName());
        wxRedPack.setMsgAppId(wxRedPackDto.getMsgAppId());
        wxRedPack.setActName(wxRedPackDto.getActName());
        wxRedPack.setRemark(wxRedPackDto.getRemark());
        wxRedPack.setReOpenId(wxRedPackDto.getReOpenId());
        wxRedPack.setSendName(wxRedPackDto.getSendName());
        wxRedPack.setAmount(new BigDecimal(wxRedPackDto.getTotalAmount()));
        wxRedPack.setTotalNum(wxRedPackDto.getTotalNum());
        wxRedPack.setClientIp(wxRedPackDto.getClientIp());
        wxRedPack.setWishing(wxRedPackDto.getWishing());
        wxRedPack.setRedPackOrderNo(wxRedPackDto.getRedpackOrderNo());
        wxRedPack.setSceneId(wxRedPackDto.getSceneId());
    }

    void saveLog(String status,WxRedPack wxRedPack,String hblist){
        WxRedPackLog wxRedPackLog = new WxRedPackLog();
        wxRedPackLog.setId(getId());
        wxRedPackLog.setCreateTime(DateUtil.now());
        wxRedPackLog.setStatus(status);
        wxRedPackLog.setPayRedPackOrderNo(wxRedPack.getPayRedPackOrderNo());
        wxRedPackLog.setRedPackOrderNo(wxRedPack.getRedPackOrderNo());

        wxRedPackLog.setUnitId(wxRedPack.getUnitId());
        wxRedPackLog.setMemberId(wxRedPack.getMemberId());
        wxRedPackLog.setSendName(wxRedPack.getSendName());
        wxRedPackLog.setTotalNum(wxRedPack.getTotalNum());
        wxRedPackLog.setAmount(wxRedPack.getAmount());
        wxRedPackLog.setReOpenId(wxRedPack.getReOpenId());
        wxRedPackLog.setHblist(hblist);
        wxRedPackLogDao.save(wxRedPackLog);
    }

    /**
     * 调接口查询结果
     * @param redPackOrderNo
     * @return
     */
    @Override
    @Transactional
    public int query(String redPackOrderNo) {
        Optional<WxRedPack> op = wxRedPackDao.findByRedPackOrderNo(redPackOrderNo);
        if(op.isPresent()
                && !op.get().getStatus().equals(0)
                && op.get().getTotalNum().equals(1)    //普通红包只查一次
                && !op.get().getStatus().equals(1)){    //如果成功则返回
            return 3;
        }

        WxRedPack wxRedPack = op.get();

        //查询发红包状态
        JSONObject jo = new WxRedPackUtil().query(op.get().getPayRedPackOrderNo());
        String status = jo.getString("status");

        if("FAIL".equals(status)){
            refundAmount(wxRedPack.getUnitId(),wxRedPack.getAmount());
            wxRedPack.setStatus(WxRedPack.StatusType.FAIL.index);
            //更新时间
            wxRedPack.setUpdateTime(DateUtil.now());
            wxRedPackDao.save(wxRedPack);
            //存记录
            saveLog(status,wxRedPack,null);
            return 3;
        }else

        //如果发生退款
        if("REFUND".equals(status)){
            refundAmount(wxRedPack.getUnitId(),wxRedPack.getAmount());
            wxRedPack.setStatus(WxRedPack.StatusType.REFUND.index);
            //存记录
            saveLog(status,wxRedPack,null);
        }else

        //红包全部已接收
        if("RECEIVED".equals(status)){
            wxRedPack.setStatus(WxRedPack.StatusType.RECEIVED.index);
            //存记录
            saveLog(status,wxRedPack,jo.getJSONArray("hbinfo").toString());
        }

        //更新时间
        wxRedPack.setUpdateTime(DateUtil.now());
        wxRedPackDao.save(wxRedPack);
        return 0;

    }

    void refundAmount(String unitId,BigDecimal amount){

        //获取支付渠道商户号
        PayChannelAccount payChannelAccount = payChannelAccountServ.findByUnitIdAndPayChannel(unitId, "wxpay");
        if (null == payChannelAccount
                || null == payChannelAccount.getPayChannelAccount()) {
            return;
        }

        payChannelAccountServ.unFreezeBalance(payChannelAccount,amount,true);
    }



    @Override
    public Map findWxRedPackOrderByUnit(Finder finder, String unitId) {
        Page page = findByUnit(finder,unitId);
        return FastJsonUtil.get(true,
                RespTips.SUCCESS_CODE.code,
                ((List<WxRedPack>) page.getItems())
                        .stream()
                        .map(wxRedPack->FastJsonUtil.getJson(wxRedPack.getBaseJson()))
                        .toArray(),
                page.getTotalCount()
        );
    }

    Page findByUnit(Finder finder, String unitId){
        StringJoiner hql = new StringJoiner(" ");
        hql.add("select w from WxRedPack w where w.unitId='"+unitId+"'");
        if(!CheckUtil.isEmpty(finder.getStatus()))
            hql.add(" and w.status="+finder.getStatus());
        hql.add("order by w.updateTime desc");
        return wxRedPackDao.findPageByHql(hql.toString(),finder.getPageSize(),finder.getStartIndex());
    }
}
