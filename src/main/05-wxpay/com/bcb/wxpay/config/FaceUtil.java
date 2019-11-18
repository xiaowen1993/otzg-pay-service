//package com.bcb.wxpay.config;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class FaceUtil {
//
//    public void doFaceRecognize() {
//        // 详细的参数配置表可见上方的“接口参数表”
//        Map<String, String> m1 = new HashMap<String, String>();
//        m1.put("appid", "填您的公众号"); // 公众号，必填
//        m1.put("mch_id", "填您的商户号"); // 商户号，必填
////        m1.put("sub_appid", "xxxxxxxxxxxxxx"); // 子商户公众账号ID(非服务商模式不填)
////        m1.put("sub_mch_id", "填您的子商户号"); // 子商户号(非服务商模式不填)
//        m1.put("store_id", "填您的门店编号"); // 门店编号，必填
//        m1.put("face_authtype", "FACEID-ONCE"); // 人脸识别模式， FACEID-ONCE`: 人脸识别(单次模式) FACEID-LOOP`: 人脸识别(循环模式), 必填
//        m1.put("authinfo", "填您的调用凭证"); // 调用凭证，详见上方的接口参数
////        m1.put("ask_unionid", "1"); // 是否获取union_id    0：获取    1：不获取
//        WxPayFace.getInstance().getWxpayfaceUserInfo(m1, new IWxPayfaceCallback() {
//            @Override
//            public void response(Map info) throws RemoteException {
//                if (info == null) {
//                    new RuntimeException("调用返回为空").printStackTrace();
//                    return;
//                }
//                String code = (String) info.get("return_code"); // 错误码
//                String msg = (String) info.get("return_msg"); // 错误码描述
//                String openid = info.get("openid").toString(); // openid
//                String sub_openid = "";
//                if (info.get("sub_openid") != null) sub_openid = info.get("sub_openid").toString(); // 子商户号下的openid(服务商模式)
//                String nickName = info.get("nickname").toString(); // 微信昵称
//                if (code == null || openid == null || nickName == null || !code.equals("SUCCESS")) {
//                    new RuntimeException("调用返回非成功信息,return_msg:" + msg + "   ").printStackTrace();
//                    return ;
//                }
//                /*
//                获取union_id逻辑，传入参数ask_unionid为"1"时使用
//                String unionid_code = "";
//                if (info.get("unionid_code") != null) unionid_code = info.get("unionid_code").toString();
//                if (TextUtils.equals(unionid_code,"SUCCESS")) {
//                    //获取union_id逻辑
//                } else {
//                    String unionid_msg = "";
//                    if (info.get("unionid_msg") != null) unionid_msg = info.get("unionid_msg").toString();
//                    //处理返回信息
//                }
//                */
//       	        /*
//       	        在这里处理您自己的业务逻辑
//       	        需要注意的是：
//       	            1、上述注释中的内容并非是一定会返回的，它们是否返回取决于相应的条件
//       	            2、当您确保要解开上述注释的时候，请您做好空指针的判断，不建议直接调用
//       	         */
//            }
//        });
//    }
//}
