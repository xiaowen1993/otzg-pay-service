package com.bcb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VehicleUtil {
    /**
     * 判断字符串是否是车牌号
     * @author G/2016年12月21日 上午9:53:05
     * @param numberPlate
     * @return
     */
    public final static boolean isNumberPlate(String numberPlate){
        boolean temp = false;
        if(numberPlate==null || numberPlate.length()<7 || numberPlate.length()>10){
            return temp;
        }
        Pattern p=Pattern.compile("^[\u4e00-\u9fa5]{1}[A-Z0-9]{2,8}$");
        Matcher m=p.matcher(numberPlate);
        if(m.find()){
            temp =  true;
        }
        return temp;
    }
}
