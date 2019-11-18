package com.bcb.log.controller;

import com.bcb.base.BaseController;
import com.bcb.log.util.LogUtil;
import com.bcb.util.RespTips;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("logController")
public class LogController extends BaseController {
    /**
     * @author admin1
     */
    @RequestMapping("/log/pay")
    public void payLog(String date){
        sendHtml(LogUtil.readTradeLog(LogUtil.getFileSavePath(),date));
    }

    /**
     * @author admin2
     */
    @RequestMapping("/log/sys")
    public void sysLog(String date){
        sendHtml(LogUtil.readSysErrorLog(LogUtil.getFileSavePath(),date));
    }

    /**
     * @author admin3
     */
    @RequestMapping("/log/web")
    public void webLog(String date){
        sendHtml(LogUtil.readWebAccessLog(LogUtil.getFileSavePath(),date));
    }

    /**
     * @author admin4
     *
     */
    @RequestMapping(value="/log/info")
    public void test(){
        JSONObject jo = new JSONObject();
        jo.put("version",LogUtil.getVersion());
        jo.put("server-url",LogUtil.getServUrl());
        jo.put("file-save-path",LogUtil.getFileSavePath());
        jo.put("local-path",LogUtil.getLocalPath());
        jo.put("server-ip-path",LogUtil.getServerIpPath());
        //返回结果
        sendJson(true, RespTips.SUCCESS_CODE.code,jo);
    }

    @RequestMapping(value="/test/upload/img",method = RequestMethod.POST)
    public void testUploadImg(String mchId,MultipartFile file){
        P("mchid="+mchId);
        P("file Name="+file.getName());
        P("file o Name="+file.getOriginalFilename());
        P("file contentType="+file.getContentType());
        P("file size="+file.getSize());
        sendJson(true, mchId,file.getName());
    }

}
