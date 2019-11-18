//package com.bcb.pay.service;
//
//import com.bcb.base.AbstractServ;
//import com.bcb.base.Finder;
//import com.bcb.base.Page;
//import com.bcb.base.RedisService;
//import com.bcb.member.entity.Member;
//import com.bcb.member.util.Password;
//import com.bcb.pay.dao.AccountDao;
//import com.bcb.pay.dao.AccountLogDao;
//import com.bcb.pay.entity.Account;
//import com.bcb.pay.entity.AccountLog;
//import com.bcb.util.*;
//import net.sf.json.JSONObject;
//import org.redisson.Redisson;
//import org.redisson.api.RLock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SessionCallback;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class AccountServImpl extends AbstractServ implements AccountServ {
//
//    @Autowired
//    AccountDao accountDao;
//
//    @Autowired
//    AccountLogDao accountLogDao;
//
//    @Override
//    public Account findById(Long id) {
//        return accountDao.findById(id).orElse(null);
//    }
//
//    @Override
//    @Transactional
//    public Account saveAccount(Member member) {
//        Account account = accountDao.findByMemberId(member.getId());
//        if (CheckUtil.isEmpty(account)) {
//            account = new Account(member);
//        }
//        return accountDao.save(account);
//    }
//
//    @Override
//    public Account findByMember(Long memberId) {
//        return accountDao.findByMemberId(memberId);
//    }
//
//
//    @Override
//    public Account findPlatform() {
//        return accountDao.findPlatform();
//    }
//
//    @Override
//    public Map getStatistics(Long accountId) {
//        String sql = "select " +
//                " COALESCE(sum(if(to_days(al.create_Time)=to_days(now()),al.amount,0)),0) as todayAmount," +        //当日收益额
//                " COALESCE(sum(if(to_days(now())-to_days(al.create_Time)<=7,al.amount,0)),0) as sevenDaysAmount," +      //7日收益额
//                " COALESCE(sum(al.amount),0) as allAmount" +                                            //全部收益额               //全部收益额
//                " from account_log al" +
//                " join account a on al.account_id = a.id " +
//                " where a.id= " + accountId;
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql);
//        return result.stream().findFirst().get();
//    }
//
//    @Override
//    public Map getUnitStatistics(String account) {
//        String sql = "select " +
//                " COALESCE(sum(if(to_days(al.create_Time)=to_days(now()),al.amount,0)),0) as todayAmount," +        //当日收益额
//                " COALESCE(sum(if(to_days(now())-to_days(al.create_Time)<=7,al.amount,0)),0) as sevenDaysAmount," +      //7日收益额
//                " COALESCE(sum(al.amount),0) as allAmount" +                                            //全部收益额               //全部收益额
//                " from account_log al" +
//                " where al.member_account= '" + account+"'";
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql);
//        return result.stream().findFirst().get();
//    }
//
//    @Override
//    public int boundPayChannel(String payChannelId, String payChannelAccount, String payChannelAccountName, String payChannel, Member member) {
//        return 0;
//    }
//
//    @Override
//    @Transactional
//    public int savePayPassword(String password, Member member) {
//        Account account = accountDao.findByMemberId(member.getId());
//        if (CheckUtil.isEmpty(account)) {
//            return 1;
//        }
//
//        //支付密码盐值
//        account.setPassword(Password.encode(member.getId().toString(), password));
//        account.setUpdateTime(DateUtil.now());
//        accountDao.save(account);
//        return 0;
//    }
//
//    @Override
//    public int checkPayPassword(String password, Long memberId) {
//        Account account = accountDao.findByMemberId(memberId);
//        //如果账户为空返回失败
//        if (CheckUtil.isEmpty(account)) {
//            return 1;
//        } else
//            //如果输入的密码为空并且未创建支付密码，通过验证
//            if (CheckUtil.isEmpty(password)
//                    && CheckUtil.isEmpty(account.getPassword())) {
//                return 0;
//            } else
//                //如果密码相同，通过验证
//                if (Password.isSame(memberId.toString(), password, account.getPassword())) {
//                    return 0;
//                } else {
//                    return 1;
//                }
//    }
//
//    @Override
//    public boolean checkProfitAmount(Long payerAccountId, BigDecimal profitAmount) {
//        BigDecimal profitAmount1 = accountDao.findProfitAmount(payerAccountId);
//        P("付款人需要支付的金额=" + profitAmount);
//        P("付款人账户id=" + payerAccountId + " 可支付余额=" + profitAmount1);
//        if (profitAmount1.compareTo(profitAmount) >= 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean checkAccount(Long memberId) {
//        Integer status = accountDao.findAccountStatusByMember(memberId);
//        if (status != 1) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public Map findByMemberId(Long memberId) {
//        Account ac = accountDao.findByMemberId(memberId);
//        if (CheckUtil.isEmpty(ac)) {
//            return null;
//        }
//
//        //获取未读收益记录
////        Map<String, Object> al = getCountNotRead(ac.getId());
//
////        Map<String, Object> all = getSumAccount(ac.getId());
//
//        Map jo = ac.getJson();
//
////        jo.put("directProfitNotRead", al.get("direct") != null ? al.get("direct") : 0);   //直接受益
////        jo.put("firstProfitNotRead", al.get("first") != null ? al.get("first") : 0);      //一级受益
////        jo.put("secondProfitNotRead", al.get("second") != null ? al.get("second") : 0);   //二级受益
////        jo.put("thirdProfitNotRead", al.get("third") != null ? al.get("third") : 0);      //三级受益
//
////        jo.put("directProfitTotal", all.get("directFitTotal") != null ? FuncUtil.FormatPercent(Double.valueOf(all.get("directFitTotal").toString()),2) : 0);
//        return jo;
//    }
//
//    Map<String, Object> getSumAccount(Long accountId) {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("SELECT")
//                .add("COALESCE(sum(IF(al.profit_type = 0, amount, 0)),0) AS directFitTotal")
//                .add("FROM account_log al")
//                .add("LEFT JOIN account a ON al.account_id = a.id")
//                .add("WHERE a.id = " + accountId);
//
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        if (CheckUtil.isEmpty(result)) {
//            return null;
//        }
//        return result.get(0);
//    }
//
//    Map<String, Object> getCountNotRead(Long accountId) {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("select")
//                .add("COALESCE(ROUND(sum(if(al.profit_type=0,1,0)),2),0) as direct,")
//                .add("COALESCE(ROUND(sum(if(al.profit_type=1,1,0)),2),0) as first,")
//                .add("COALESCE(ROUND(sum(if(al.profit_type=2,1,0)),2),0) as second,")
//                .add("COALESCE(ROUND(sum(if(al.profit_type=3,1,0)),2),0) as third")
//                .add("from account_log al")
//                .add("left join account a on al.account_id =a.id")
//                .add("where a.id =" + accountId)
//                .add("and al.is_read=0");
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        if (CheckUtil.isEmpty(result)) {
//            return null;
//        }
//        return result.get(0);
//
//    }
//
//
//    @Override
//    public JSONObject findAccountLog(Finder finder, Long accountId,String mobile) {
//        Page page = findPageAccountLogList(finder, accountId,mobile);
//        List<AccountLog> accountLogList = (List<AccountLog>) page.getItems();
//        return JsonUtil.get(true, RespTips.SUCCESS_CODE.code, accountLogList.stream().map(AccountLog::getJson).toArray(), page.getTotalCount());
//    }
//
//    Page findPageAccountLogList(Finder finder, Long accountId,String mobile) {
//        StringJoiner hql = new StringJoiner(" ")
//                .add("select al ")
//                .add("from AccountLog al")
//                .add("left join al.account ac")
//                .add("where 1=1");
//
//        if (!CheckUtil.isEmpty(accountId)
//                && !CheckUtil.isEmpty(mobile)) {
//            hql.add("and (ac.id=" + accountId + " or al.memberAccount = '"+mobile+"')");
//        }else if (!CheckUtil.isEmpty(accountId)){
//            hql.add("and (ac.id=" + accountId + ")");
//        }else if(!CheckUtil.isEmpty(mobile)){
//            hql.add("and (al.memberAccount = '"+mobile+"')");
//        }
//
//        if(!CheckUtil.isEmpty(finder.getStartTime())) {
//            hql.add("and al.createTime >= '" + finder.getStartTime() + "'");
//        }
//        if(!CheckUtil.isEmpty(finder.getEndTime())) {
//            hql.add("and al.createTime <= '" + finder.getEndTime() + "'");
//        }
//
//        if (!CheckUtil.isEmpty(finder.getStatus())) {
//            hql.add("and al.profitType = " + finder.getStatus());
//        }
//
//        if (!CheckUtil.isEmpty(finder.getKeyword())) {
//            hql.add("and (locate('" + finder.getKeyword() + "',al.details)>0")
//                    .add("or locate('" + finder.getKeyword() + "',al.payOrdersNo)>0)");
//        }
//
//        hql.add(" ORDER BY al.createTime DESC");
//
//        return accountLogDao.findPageByHql(hql.toString(), finder.getPageSize(), finder.getStartIndex());
//    }
//
//    @Override
//    public Map findProfitAccountLog(Long accountId, String profitType, Finder finder) {
//        Page page = findProfitAccountLogPage(accountId, profitType, finder);
//        List<Map<String, Object>> map = (List<Map<String, Object>>) page.getItems();
//        return JsonUtil.get(true, RespTips.SUCCESS_CODE.code, map, page.getTotalCount());
//    }
//
//    Page findProfitAccountLogPage(Long accountId, String profitType, Finder finder) {
//        String sql = " SELECT" +
//                " al.id AS Id," +
//                " al.amount," +
//                " al.balance," +
//                " al.create_time AS createTime," +
//                " al.details," +
//                " al.pay_channel AS payChannel," +
//                " al.pay_orders_no AS payOrdersNo," +
//                " ap.`name`," +
//                " p.appid" +
//                " from" +
//                " account_log al" +
//                " LEFT JOIN pay_orders p ON p.number = al.pay_orders_no" +
//                " WHERE" +
//                " account_id = " + accountId;
//        return accountLogDao.findPageBySql(sql, finder.getPageSize(), finder.getStartIndex());
//    }
//
//    @Override
//    @Transactional
//    public int clearReadMarkByProfitType(String account, Integer profitType) {
//        accountLogDao.clearReadMarkByProfitType(account, profitType);
//        return 0;
//    }
//
//    @Override
//    public Map findUnitDayAmountByDateBetween(String startDate, String endDate, String account) {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("select DATE_FORMAT(al.create_time,'%Y-%m-%d') day,")
//                .add("COALESCE(ROUND(sum(al.amount),2),0) as amount")                             //每日收益额
//                .add("from account_log al")
//                .add("where al.member_account='"+account+"'")
//                .add("and al.create_time between '" + startDate + "' and '" + endDate + "'")
//                .add("group by to_days(al.create_Time)");
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        //返回结果数据
//        Map map = new JSONObject();
//        map.put("success",true);
//        map.put("code",RespTips.SUCCESS_CODE.code);
//        map.put("data",result.stream().toArray());
//        map.put("count",FuncUtil.getDoubleScale(result.stream().filter(m->!CheckUtil.isEmpty(result.stream())).mapToDouble(m->new Double(m.get("amount").toString())).sum()));
//
//        return map;
//    }
//
//    @Override
//    public Map findUnitProfitCountAndSumByDateBetween(String startDate, String endDate,String account) {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("select count(al.id) as count,")
//                .add("COALESCE(ROUND(sum(al.amount),2),0) as sum")
//                .add("from account_log al")
//                .add("where al.member_account='"+account+"'");
//
//        //查询日期区间
//        if (!CheckUtil.isEmpty(startDate)
//                && !CheckUtil.isEmpty(endDate)) {
//            sql.add("and al.create_time between '" + startDate + "' and '" + endDate + "'");
//        }
//
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        if (CheckUtil.isEmpty(result)) {
//            return null;
//        }
//        return result.get(0);
//    }
//
//
//
//
//
//    //=============================平台业务======================================
//    @Override
//    public Map findDayAmountByDateBetween(Long accountId, String startDate, String endDate) {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("select DATE_FORMAT(al.create_time,'%Y-%m-%d') day,")
//                .add("COALESCE(ROUND(sum(al.amount),2),0) as amount")                       //每日收益额
//                .add("from account_log al ")
//                .add("join account a on al.account_id = a.id ")
//                .add("where a.id= " + accountId)
//                .add("and al.create_time between '" + startDate + "' and '" + endDate + "'")
//                .add("group by to_days(al.create_Time)");                                   //统计以实际发生的订单
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        //返回结果数据
//        Map map = new JSONObject();
//        map.put("success",true);
//        map.put("code",RespTips.SUCCESS_CODE.code);
//        map.put("data",result.stream().toArray());
//        map.put("count",FuncUtil.getDoubleScale(result.stream().filter(m->!CheckUtil.isEmpty(result.stream())).mapToDouble(m->new Double(m.get("amount").toString())).sum()));
//
//        return map;
//    }
//
//    @Override
//    public JSONObject findAccountLog(Finder finder) {
//        Page page = findPageAccountLogList(finder,null,null);
//        List<AccountLog> accountLogList = (List<AccountLog>) page.getItems();
//        return JsonUtil.get(true, RespTips.SUCCESS_CODE.code, accountLogList.stream().map(AccountLog::getJson).toArray(), page.getTotalCount());
//    }
//
//    @Override
//    public Object[] findMemberProfitSort() {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("select m.id,m.name,COALESCE(ROUND(sum(al.amount),2),0) as amount")
//                .add("from member m")
//                .add("left join account a on a.member_id = m.id")
//                .add("left join account_log al on a.id = al.account_id")
//                .add("where m.type=0")
//                .add("group by m.id")
//                .add("ORDER BY amount DESC")
//                .add("limit 4");
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        return result.stream().toArray();
//    }
//
//
//    @Override
//    public Object[] findUnitProfitSort() {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("select u.id,u.name,COALESCE(ROUND(sum(al.amount),2),0) as amount")
//                .add("from unit u")
//                .add("join member m on u.member_id = m.id")
//                .add("left join account_log al on al.member_account = m.account")
//                .add("where m.type=1")
//                .add("group by m.id")
//                .add("ORDER BY amount DESC")
//                .add("limit 4");
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        return result.stream().toArray();
//    }
//
//    @Override
//    public Map findMemberProfitTotalAndAvg() {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("SELECT")
//                .add("COALESCE(ROUND(sum(al.amount),2),0) as sumAmount,")
//                .add("COALESCE(ROUND(avg(al.amount),2),0) as avgAmount")
//                .add("from member m")
//                .add("join account a on a.member_id = m.id")
//                .add("join account_log al on m.id = al.account_id")
//                .add("where m.type=0");
//
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        if (CheckUtil.isEmpty(result)) {
//            return null;
//        }
//        return result.get(0);
//    }
//
//    @Override
//    public Map findUnitProfitTotalAndAvg() {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("SELECT")
//                .add("COALESCE(ROUND(sum(al.amount),2),0) as sumAmount,")
//                .add("COALESCE(ROUND(avg(al.amount),2),0) as avgAmount")
//                .add("from member m")
//                .add("join account_log al on m.account = al.member_account")
//                .add("where m.type=1");
//
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        if (CheckUtil.isEmpty(result)) {
//            return null;
//        }
//        return result.get(0);
//    }
//
//    @Override
//    public Map findMemberProfitCountAndSumByDateBetween(String startDate, String endDate, Long memberId) {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("select count(al.id) as count,")
//                .add("COALESCE(ROUND(sum(al.amount),2),0) as sum")
//                .add("from member m")
//                .add("left join account a on a.member_id = m.id")
//                .add("left join account_log al on a.id = al.account_id")
//                .add("where 1=1");
//
//        //会员id不为空
//        if (!CheckUtil.isEmpty(memberId)) {
//            sql.add("and m.id=" + memberId);
//        }
//        //查询日期区间
//        if (!CheckUtil.isEmpty(startDate)
//                && !CheckUtil.isEmpty(endDate)) {
//            sql.add("and al.create_time between '" + startDate + "' and '" + endDate + "'");
//        }
//
////        sql.add("group by to_days(al.create_Time)");
//
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        if (CheckUtil.isEmpty(result)) {
//            return null;
//        }
//        return result.get(0);
//    }
//
//    /**
//     * 按日期获取推广员的日交易记录
//     *
//     * @param startDate
//     * @param endDate
//     * @return
//     */
//    @Override
//    public Map findMemberDayAmountByDateBetween(String startDate, String endDate, Long memberId) {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("select ")
//                .add("m.id as id,")
//                .add("DATE_FORMAT(al.create_time,'%Y-%m-%d') day,")
//                .add("COALESCE(ROUND(sum(al.amount),2),0) as amount")                            //每日收益额
//                .add("from member m")
//                .add("left join account a on a.member_id = m.id")
//                .add("left join account_log al on a.id = al.account_id")
////                .add("where m.type=0");                                               //可以是商户和推广员
//                .add("where 1=1");
//
//        //会员id不为空
//        if (!CheckUtil.isEmpty(memberId)) {
//            sql.add("and m.id=" + memberId);
//        }
//        //查询日期区间
//        sql.add("and al.create_time between '" + startDate + "' and '" + endDate + "'")
//                .add("group by to_days(al.create_Time)");
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//
//        //返回结果数据
//        Map map = new JSONObject();
//        map.put("success",true);
//        map.put("code",RespTips.SUCCESS_CODE.code);
//        map.put("data",result.stream().toArray());
//        map.put("count",FuncUtil.getDoubleScale(result.stream().filter(m->!CheckUtil.isEmpty(result.stream())).mapToDouble(m->new Double(m.get("amount").toString())).sum()));
//
//        return map;
//    }
//
//    @Override
//    public Map findUnitDayAmountByDateBetween(String startDate, String endDate) {
//        StringJoiner sql = new StringJoiner(" ")
//                .add("select DATE_FORMAT(al.create_time,'%Y-%m-%d') day,")
//                .add("COALESCE(ROUND(sum(al.amount),2),0) as amount")                             //每日收益额
//                .add("from member m")
//                .add("left join account_log al on m.account = al.member_account")
//                .add("where m.type=1")
//                .add("and al.create_time between '" + startDate + "' and '" + endDate + "'")
//                .add("group by to_days(al.create_Time)");
//        List<Map<String, Object>> result = accountLogDao.findMapBySql(sql.toString());
//        //返回结果数据
//        Map map = new JSONObject();
//        map.put("success",true);
//        map.put("code",RespTips.SUCCESS_CODE.code);
//        map.put("data",result.stream().toArray());
//        map.put("count",FuncUtil.getDoubleScale(result.stream().filter(m->!CheckUtil.isEmpty(result.stream())).mapToDouble(m->new Double(m.get("amount").toString())).sum()));
//
//        return map;
//    }
//
//    @Override
//    public JSONObject findUnitProfitByDateBetweenGroupByMember(Finder finder) {
//        Page page = findPageUnitAmountByDateBetweenGroupByMember(finder);
//        List<Map<String, Object>> map = (List<Map<String, Object>>) page.getItems();
//        return JsonUtil.get(true, RespTips.SUCCESS_CODE.code, map.stream().toArray(), page.getTotalCount());
//    }
//
//    /**
//     * 推广员下属商户的区间收益统计
//     * 1、返回推广员姓名、区域、商户收益和、推广员注册日期
//     *
//     * @param finder
//     * @return
//     */
//    Page findPageUnitAmountByDateBetweenGroupByMember(Finder finder) {
//        //统计数量
//        StringJoiner countSql = new StringJoiner(" ");
//        countSql.add("select count(m2.id)")
//                .add("from member m2")                          //m2是推广员
//                .add("where m2.type=0");
//
//        if (!CheckUtil.isEmpty(finder.getKeyword())) {
//            countSql.add("and (locate('" + finder.getKeyword() + "',m2.name)>0)");
//        }
//
//        if (!CheckUtil.isEmpty(finder.getAreaName())) {
//            countSql.add("and (locate('" + finder.getAreaName() + "',m2.area_name)>0)");
//        }
//        //统计推广员数量
//        Long totalCount = accountLogDao.getCountBySql(countSql.toString());
//
//
//        //数据集合
//        StringJoiner listSql = new StringJoiner(" ");
//        listSql.add("select ")
//                .add("m2.id as id,")
//                .add("m2.name as name,")
//                .add("m2.area_name as areaName,")
//                .add("COALESCE(ROUND(sum(al.amount),2),0) as amount,")                              //每日收益额
//                .add("DATE_FORMAT(m2.create_time,'%Y-%m-%d %H:%i:%s') as createTime")               //m2是推广员注册日期
//                .add("from member m2")
//                .add("left join promotion p on p.first_member_id = m2.id")                          //m2是推广员
//                .add("left join member m on m.id = p.member_id")                                    //m是商户
//                .add("left join account a on a.member_id = m.id")
//                .add("left join account_log al on a.id = al.account_id");
//
//        StringJoiner sql = new StringJoiner(" ")
//                .add("where m2.type=0");
//
//        if (!CheckUtil.isEmpty(finder.getKeyword())) {
//            sql.add("and (locate('" + finder.getKeyword() + "',m2.name)>0)");
//        }
//
//        if (!CheckUtil.isEmpty(finder.getAreaName())) {
//            sql.add("and (locate('" + finder.getAreaName() + "',m2.area_name)>0)");
//        }
//
//        if (!CheckUtil.isEmpty(finder.getStartTime())
//                && !CheckUtil.isEmpty(finder.getEndTime())) {
//            sql.add("and al.create_time between '" + finder.getStartTime() + "' and '" + finder.getEndTime() + "'");
//        }
//
//        sql.add("group by m2.id");
//
//        sql.add("order by m2.id");
//        List<Map<String, Object>> items = accountLogDao.findMapBySql(listSql.merge(sql).toString(), finder.getPageSize(), finder.getStartIndex());
//
//        //返回分页数据
//        return new Page(items, totalCount, finder.getPageSize(), finder.getStartIndex());
//    }
//
//
//    @Override
//    public JSONObject findUnitProfitByDateBetween(Finder finder) {
//        Page page = findPageUnitAmountByDateBetween(finder);
//        List<Map<String, Object>> map = (List<Map<String, Object>>) page.getItems();
//        return JsonUtil.get(true, RespTips.SUCCESS_CODE.code, map.stream().toArray(), page.getTotalCount());
//    }
//    Page findPageUnitAmountByDateBetween(Finder finder) {
//        //统计推广员数量
//        StringJoiner countSql = new StringJoiner(" ");
//        countSql.add("select count(m.id)")
//                .add("from member m")                          //m是推广员
//                .add("left join unit u on u.member_id = m.id")
//                .add("where u.id is not null and m.type=1");
//        //统计推广员数量
//        Long totalCount = accountLogDao.getCountBySql(countSql.toString());
//
//        //数据集合
//        StringJoiner listSql = new StringJoiner(" ");
//        listSql.add("select m.id as id,")
//                .add("m2.name as promoterName,")
//                .add("u.name as unitName,")
//                .add("m.area_name as areaName,");
//
//        if(!CheckUtil.isEmpty(finder.getStartTime())) {
//            listSql.add("sum(if(al.create_time BETWEEN '" + finder.getStartTime() + "' and '"+finder.getEndTime()+"',al.amount,0)) as amount,");                              //每日收益额
//        }else{
//            listSql.add("COALESCE(sum(al.amount),0) as amount,");                              //收益额
//        }
//
//        listSql.add("DATE_FORMAT(m.create_time,'%Y-%m-%d %H:%i:%s') as createTime")
//                .add("from member m")                                                      //m是商户
//                .add("left join unit u on u.member_id = m.id")
//                .add("left join promotion p on  p.member_id = m.id")
//                .add("left join member m2 on m2.id = p.first_member_id")                   //m2是推广员
//                .add("left join account_log al on m.account = al.member_account");
//
//        StringJoiner sql = new StringJoiner(" ")
//                .add("where u.id is not null and m.type=1");
//
//        if (!CheckUtil.isEmpty(finder.getKeyword())) {
//            sql.add("and (locate('" + finder.getKeyword() + "',u.name)>0 " +
//                    " or locate('" + finder.getKeyword() + "',m2.name)>0 " +
//                    " or locate('" + finder.getKeyword() + "',m.area_name)>0 )");
//        }
//
//        sql.add("group by m.id");
//        sql.add("order by m.id");
//        List<Map<String, Object>> items = accountLogDao.findMapBySql(listSql.merge(sql).toString(), finder.getPageSize(), finder.getStartIndex());
//
//        //返回分页数据
//        return new Page(items, totalCount, finder.getPageSize(), finder.getStartIndex());
//    }
//
//    @Override
//    public JSONObject findMemberProfitByDateBetween(Finder finder) {
//        Page page = findPageMemberAmountByDateBetween(finder);
//        List<Map<String, Object>> map = (List<Map<String, Object>>) page.getItems();
//        return JsonUtil.get(true, RespTips.SUCCESS_CODE.code, map.stream().toArray(), page.getTotalCount());
//    }
//
//    /**
//     * 推广员的区间收益统计
//     * 1、返回推广员姓名、区域、商户收益和、推广员注册日期
//     *
//     * @param finder
//     * @return
//     */
//    Page findPageMemberAmountByDateBetween(Finder finder) {
//        //统计推广员数量
//        StringJoiner countSql = new StringJoiner(" ");
//        countSql.add("select count(m.id)")
//                .add("from member m")                          //m是推广员
//                .add("where m.type=0");
//        //统计推广员数量
//        Long totalCount = accountLogDao.getCountBySql(countSql.toString());
//
//        //数据集合
//        StringJoiner listSql = new StringJoiner(" ");
//        listSql.add("select m.id as id,")
//                .add("m.name as name,")
//                .add("m.area_name as areaName,");
//
//        if(!CheckUtil.isEmpty(finder.getStartTime())) {
//            listSql.add("sum(if(al.create_time BETWEEN '" + finder.getStartTime() + "' and '"+finder.getEndTime()+"',1,0)) as amount,");                              //每日收益额
//        }else{
//            listSql.add("COALESCE(sum(al.amount),0) as amount,");                              //收益额
//        }
//
//        listSql.add("DATE_FORMAT(m.create_time,'%Y-%m-%d %H:%i:%s') as createTime")       //m是推广员注册日期
//                .add("from member m")
//                .add("left join account a on a.member_id = m.id")
//                .add("left join account_log al on a.id = al.account_id");
//
//        StringJoiner sql = new StringJoiner(" ")
//                .add("where m.type=0");
//
//        if (!CheckUtil.isEmpty(finder.getKeyword())) {
//            sql.add("and (locate('" + finder.getKeyword() + "',m.name)>0)");
//        }
//
//        if (!CheckUtil.isEmpty(finder.getAreaName())) {
//            sql.add("and (locate('" + finder.getAreaName() + "',m.area_name)>0)");
//        }
//        sql.add("group by m.id");
//        sql.add("order by m.id");
//        List<Map<String, Object>> items = accountLogDao.findMapBySql(listSql.merge(sql).toString(), finder.getPageSize(), finder.getStartIndex());
//
//        //返回分页数据
//        return new Page(items, totalCount, finder.getPageSize(), finder.getStartIndex());
//    }
//
//
//    /**
//     * 平台的区间收益分页
//     *
//     * @param finder
//     * @return 收益、类型、来源、日期
//     */
//    Page findPagePlatformAmountByDateBetween(Finder finder) {
//        //数据集合
//        StringJoiner listSql = new StringJoiner(" ");
//        listSql.add("select COALESCE(ROUND(sum(al.amount),2),0) as amount,")
//                .add("al.details as details")
//                .add("")                              //每日收益额
//                .add("DATE_FORMAT(m.create_time,'%Y-%m-%d %H:%i:%s') as createTime")       //m是推广员注册日期
//                .add("from member m")
//                .add("left join account a on a.member_id = m.id")
//                .add("left join account_log al on a.id = al.account_id");
//
//        StringJoiner sql = new StringJoiner(" ")
//                .add("where m.type=0");
//
//        if (!CheckUtil.isEmpty(finder.getKeyword())) {
//            sql.add("and (locate('" + finder.getKeyword() + "',m.name)>0)");
//        }
//
//        if (!CheckUtil.isEmpty(finder.getAreaName())) {
//            sql.add("and (locate('" + finder.getAreaName() + "',m.area_name)>0)");
//        }
//
//        if (!CheckUtil.isEmpty(finder.getStartTime())
//                && !CheckUtil.isEmpty(finder.getEndTime())) {
//            sql.add("and al.create_time between '" + finder.getStartTime() + "' and '" + finder.getEndTime() + "'");
//        }
//
//        sql.add("group by m.id");
//
//        sql.add("order by m.id");
//        List<Map<String, Object>> items = accountLogDao.findMapBySql(listSql.merge(sql).toString(), finder.getPageSize(), finder.getStartIndex());
//
//        //返回分页数据
////        return new Page(items, totalCount, finder.getPageSize(), finder.getStartIndex());
//        return null;
//    }
//
//    @Override
//    public JSONObject findPlatformAccountLog(Finder finder, Long accountId) {
//        Page page = findPagePlatformAccountLogList(finder, accountId);
//        List<AccountLog> accountLogList = (List<AccountLog>) page.getItems();
//        return JsonUtil.get(true, RespTips.SUCCESS_CODE.code, accountLogList.stream().map(AccountLog::getJson).toArray(), page.getTotalCount());
//    }
//
//    Page findPagePlatformAccountLogList(Finder finder, Long accountId) {
//        StringJoiner hql = new StringJoiner(" ")
//                .add("select al ")
//                .add("from AccountLog al")
//                .add("join al.account ac")
//                .add("where ac.id=" + accountId);
//
//        if (!CheckUtil.isEmpty(finder.getStatus())) {
//            hql.add("and al.profitType = " + finder.getStatus());
//        }
//        if(!CheckUtil.isEmpty(finder.getStartTime())) {
//            hql.add("and al.createTime >= '" + finder.getStartTime() + "'");
//        }
//        if(!CheckUtil.isEmpty(finder.getEndTime())) {
//            hql.add("and al.createTime <= '" + finder.getEndTime() + "'");
//        }
//
//        if (!CheckUtil.isEmpty(finder.getKeyword())) {
//            hql.add("and (locate('" + finder.getKeyword() + "',al.details)>0")
//                    .add("or locate('" + finder.getKeyword() + "',al.payOrdersNo)>0)");
//        }
//
//
//        hql.add(" ORDER BY al.createTime DESC");
//
//        return accountLogDao.findPageByHql(hql.toString(), finder.getPageSize(), finder.getStartIndex());
//    }
//
//
//
//
//
//
//
//    //==========================================================================//
//
//    @Autowired
//    Redisson redisson;
//    @Autowired
//    RedisService redisService;
//
//    //利用redisson锁技术
//    @Override
//    public boolean testLock() {
//        P("start");
//        RLock lock = redisson.getLock("abc");
//        try {
//            boolean l = lock.tryLock(30, TimeUnit.SECONDS);
//            if (!l) {
//                P("false");
//                return false;
//            }
//
//            P("lock");
//            int stock = Integer.parseInt(redisService.get("stock").toString());
//            if (stock > 0) {
//                int realStock = stock - 1;
//                redisService.set("stock", realStock + "");
//                P("realStock=" + realStock);
//            } else {
//                P("stock < 1");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            P("error");
//        } finally {
//            lock.unlock();
//            P("unlock");
//        }
//        P("end");
//        return true;
//    }
//
//    @Autowired
//    RedisTemplate redisJsonTemplate;
//
//    void waitForLock(int k) {
//        try {
//            P(k+":开始休眠:"+ Instant.now());
//            Thread.sleep(new Random().nextInt(20) + 1);
//            P(k+":结束休眠:"+ Instant.now());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            P(k+":休眠失败");
//        }
//    }
//
//
//    @Override
//    public Map testWatch(Integer k){
//        return fromWatch(k);
////        return fromRedis(k);
//    }
//
//    Map fromWatch(Integer k){
//        String key = "multkey";
//        Map r= new HashMap();
//        r.put(k+":value",-1);
//
//        Runnable runable = ()-> {
//            redisJsonTemplate.execute(new SessionCallback<Object>() {
//                @Override
//                @SuppressWarnings({ "unchecked", "rawtypes" })
//                public Object execute(RedisOperations operations) {
//                    List<Object> result = null;
//                    do {
//                        P(k+":监控锁");
//                        operations.watch(key);  // watch某个key,当该key被其它客户端改变时,则会中断当前的操作
//                        P(k+":获得锁");
//
//                        Object value = operations.opsForValue().get(key);
//                        P(k+":事务前value="+value);
//                        if(null!=value && Integer.parseInt(value.toString())<1){
//                            return null;
//                        }
//
//                        operations.multi();
//                        P(k+":开启事务");
//
//                        operations.opsForValue().increment(key,-1);
////                        operations.opsForValue().getAndSet(key,Integer.parseInt(value.toString())-1);
//                        try {
//                            result = operations.exec(); //提交事务
//                            if(null == result){
//                                P(k+":提交事务失败,重试");
//                            }else{
//                                P(k+":提交事务成功="+result.get(0));
//                                r.put(k+":value",result.get(0));
//                                return null;
//                            }
//
//                        } catch (Exception e) { //如果key被改变,提交事务时这里会报异常
//                            P(k+":提交事务失败");
//                            waitForLock(k);
//                        }
//
//                    } while (null ==  result || result.isEmpty()); //如果失败则重试
//
//                    P(k+":线程执行结束");
//                    return null;
//                }
//
//            });
//        };
//
//        runable.run();
//        return r;
//    }
//
//
//}
