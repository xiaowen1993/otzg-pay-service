//package com.bcb.pay.api;
//
//import com.bcb.base.Finder;
//import com.bcb.member.api.AbstractController;
//import com.bcb.message.service.MessageServ;
//import com.bcb.pay.entity.Account;
//import com.bcb.pay.entity.PayOrders;
//import com.bcb.pay.service.AccountServ;
//import com.bcb.pay.service.PayOrdersServ;
//import com.bcb.pay.util.PayChannelType;
//import com.bcb.pay.util.PayType;
//import com.bcb.promotion.entity.Allocation;
//import com.bcb.promotion.service.AllocationServ;
//import com.bcb.util.CheckUtil;
//import com.bcb.util.FuncUtil;
//import com.bcb.util.LockUtil;
//import com.bcb.util.RespTips;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.math.BigDecimal;
//import java.util.Map;
//
//@RestController
//public class PayOrderController extends AbstractController {
//    @Autowired
//    private PayOrdersServ payOrdersServ;
//    @Autowired
//    private AllocationServ allocationServ;
//    @Autowired
//    private AccountServ accountServ;
//    @Autowired
//    private LockUtil lockUtil;
//    @Autowired
//    private MessageServ messageServ;
//
//    @RequestMapping("/member/pay/recharge")
//    public final void recharge(String token,
//                                 String appid,
//                                 String subject,
//                                 BigDecimal amount,
//                                 String details,
//                                 String payChannel,
//                                 String openId) {
//
//        if (!CheckUtil.isBigDecimal(amount)
//                || PayChannelType.hasNotFound(payChannel)
//                ) {
//            sendParamError();
//            return;
//        }
//
//        if (CheckUtil.isEmpty(subject)) {
//            subject = "余额充值";
//        }
//        if (CheckUtil.isEmpty(details)) {
//            details = "余额充值";
//        }
//
//        Account payeeAccount = accountServ.findByMember(getMember(token).getId());
//        if(CheckUtil.isEmpty(payeeAccount)){
//            sendJson(false,RespTips.DATA_NULL.code,"账户异常");
//            return;
//        }
//
//        //创建充值订单并调起支付参数返回
//        sendSuccess(payOrdersServ.recharge(appid, subject, amount, details,getMember(token).getId(), payeeAccount.getId(), payChannel,"app", openId,PayType.CHANNEL_PAY.index));
//
//    }
//
//    /**
//     * 交年费
//     * 商户交年费
//     */
//    @RequestMapping("/member/pay/payYearFee")
//    public final void payYearFee(String token,
//                                 String appid,
//                                 String subject,
//                                 BigDecimal amount,
//                                 String details,
//                                 String payChannel,
//                                 String openId) {
//
//        if (!CheckUtil.isBigDecimal(amount)
//                || PayChannelType.hasNotFound(payChannel)
//                ) {
//            sendParamError();
//            return;
//        }
//
//        if (CheckUtil.isEmpty(subject)) {
//            subject = "预交年费";
//        }
//        if (CheckUtil.isEmpty(details)) {
//            details = "预交年费";
//        }
//
//        Account payerAccount = accountServ.findByMember(getMember(token).getId());
//        if(CheckUtil.isEmpty(payerAccount)){
//            sendJson(false,RespTips.DATA_NULL.code,"商户账户异常");
//            return;
//        }
//
//        Account platformAccount = accountServ.findPlatform();
//        if(CheckUtil.isEmpty(platformAccount)){
//            sendJson(false,RespTips.DATA_NULL.code,"平台账户异常,请稍后再试");
//            return;
//        }
//
//        Allocation allocation = allocationServ.getByType(1);
//        if (CheckUtil.isEmpty(allocation)) {
//            sendJson(false,RespTips.DATA_NULL.code,"没有对应的规则");
//            return;
//        }
//
//        //创建充值订单并调起支付参数返回
//        sendSuccess(payOrdersServ.payYearFee(allocation,appid, subject, amount, details,getMember(token).getId(), payerAccount.getId(), platformAccount.getId(),payChannel, openId));
//
//    }
//
//    /**
//     * 提现申请
//     */
//    @RequestMapping("/member/pay/getCashApply")
//    public final void getCashApply(String token,
//                                   String appid,
//                                   String subject,
//                                   BigDecimal amount,
//                                   String details,
//                                   String payChannel,
//                                   String openId) {
//
//        if (!CheckUtil.isBigDecimal(amount)
//                || PayChannelType.hasNotFound(payChannel)
//                ) {
//            sendParamError();
//            return;
//        }
//
//        //账号加锁
//        if(lockUtil.lockAccount(token)){
//            //获取未处理的提现申请
//            PayOrders payOrders = payOrdersServ.findWaitByMemberAndType(getMember(token).getId(),2);
//            if(!CheckUtil.isEmpty(payOrders)){
//                sendJson(false,RespTips.APPROVAL_FAIL.code,"请等待上次提现申请结果");
//                return;
//            }
//
//            if (CheckUtil.isEmpty(subject)) {
//                subject = "提现申请";
//            }
//            if (CheckUtil.isEmpty(details)) {
//                details = "提现申请";
//            }
//
//            Account payerAccount = accountServ.findByMember(getMember(token).getId());
//            if(CheckUtil.isEmpty(payerAccount)){
//                sendJson(false,RespTips.DATA_NULL.code,"账户异常");
//                return;
//            }
//
//            //判断是否绑定支付渠道
//            if(CheckUtil.isEmpty(payerAccount.getPayChannelId())
//                    ||CheckUtil.isEmpty(payerAccount.getPayChannelAccount())){
//                sendJson(false,RespTips.DATA_NULL.code,"未绑定支付渠道");
//                return;
//            }
//
//            //创建充值订单并调起支付参数返回
//            Map map = payOrdersServ.getCashApply(appid, subject, amount, details,null,getMember(token).getId(), payerAccount.getId(),null, payChannel);
//            if(!CheckUtil.isEmpty(map)){
//                sendSuccess(map);
//                //发送获得下级推广人消息
////                messageServ.saveMessage(null,"推广员"+getMember(token).getName()+"申请提现"+amount+"元", "推广员"+getMember(token).getName()+"申请提现"+amount+"元", Message.Type.GETCASHAPPLY.code, getMember(token).getId(),Long.parseLong(map.get("id").toString()));
//            }else{
//                sendFail();
//                return;
//            }
//
//            //账号解锁
//            lockUtil.unLockAccount(token);
//        }else{
//            sendFail();
//            return;
//        }
//
//    }
//
//
//    @RequestMapping("/member/pay/getCashApplyOrder")
//    public final void getCashApplyOrder(String token,Long id) {
//        if(CheckUtil.isEmpty(id)){
//            sendParamError();
//            return;
//        }
//
//        //获取为处理的提现申请
//        PayOrders payOrders = payOrdersServ.findByMemberAndType(getMember(token).getId(),2);
//        if(CheckUtil.isEmpty(payOrders)){
//            sendJson(false,RespTips.DATA_NULL.code,RespTips.DATA_NULL.tips);
//            return;
//        }
//
//        //创建充值订单并调起支付参数返回
//        sendSuccess(payOrders.getJson());
//    }
//
//    /**
//     * 绑定账户
//     * token
//     * appid
//     */
//    @RequestMapping(value = "/member/account/bind")
//    public void bindingAccount(String token,
//                               String appid,
//                               String payChannel,
//                               String openId,
//                               String subject) {
//
//        if (CheckUtil.isEmpty(token)
//                || CheckUtil.isEmpty(subject)) {
//            sendParamError();
//            return;
//        }
//
//        Account payeeAccount = accountServ.findByMember(getMember(token).getId());
//        if(CheckUtil.isEmpty(payeeAccount)){
//            sendJson(false,RespTips.DATA_NULL.code,"商户账户异常");
//            return;
//        }
//
//        BigDecimal amount=FuncUtil.getBigDecimalScale(new BigDecimal(0.01));
//        //获取付款人单位信息,付款单位
//        sendSuccess(payOrdersServ.recharge(appid, subject, amount, "绑定用户支付宝账户", getMember(token).getId(),payeeAccount.getId(),payChannel, "app", null,PayType.BIND_ACCOUNT.index));
//    }
//
//    /**
//     * 订单分页
//     * @param finder
//     */
//    @RequestMapping(value = "/member/payOrder/find")
//    public void payOrderFind(String token,Finder finder) {
//        sendJson(payOrdersServ.findByPayOrders(finder,getMember(token).getId()));
//    }
//
//
//}
