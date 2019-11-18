//package com.bcb.file.api;
//
//import com.bcb.admin.controller.AbstractController;
//import com.bcb.file.entity.FileIndex;
//import com.bcb.file.service.FileServ;
//import com.bcb.util.CheckUtil;
//import com.bcb.util.LockUtil;
//import com.bcb.util.RespTips;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 文件上传处理类
// *
// * @Author:G./2018/2/28 9:37
// */
//
//@RestController("/file")
//public final class FileAdminApi extends AbstractController {
//
//    @Autowired
//    private FileServ fileServ;
//
//    @Autowired
//    private LockUtil lockUtil;
//
//
//    /**
//     * 通用上传图片
//     * @param file
//     */
//    @RequestMapping(value = {"/admin/{root}/image/upload"}, method = RequestMethod.POST)
//    public void uploadImg(@PathVariable("root") String root,
//                          Integer folderId,
//                          MultipartFile file) {
//
//        //判断是否是image
//        if (file == null
//                || !CheckUtil.isImage(file.getContentType())
//                ) {
//            sendParamError();
//            return;
//        }
//
//        //保存
//        Map jo = fileServ.saveLink(file, "/"+root,folderId);
//        if(CheckUtil.isEmpty(jo)){
//            sendFail();
//            return;
//        }
//
//        //返回成功并且包含路径
//        sendJson(true, RespTips.SUCCESS_CODE.code, jo);
//    }
//
//    @RequestMapping(value = "/file/delete")
//    public void fileDelete(Long[] ids) {
//        //判断是否是image
//        if (!CheckUtil.isLongArray(ids)) {
//            sendParamError();
//            return;
//        }
//
//        //删除文件
//        fileServ.deleteFiles(ids);
//
//        //返回结果
//        sendSuccess();
//    }
//
//    @RequestMapping(value = "/admin/delete_all")
//    public void delteFiles() {
//        int count = fileServ.autoDeleteFiles();
//        //返回结果
//        sendJson(true, RespTips.SUCCESS_CODE.code, count);
//    }
//
//    /**
//     * 大文件上传
//     *
//     * @param token 登录凭证
//     * @param file  上传文件
//     * @param index 当前文件切片
//     * @param total 切片总数量
//     * @author G/2018-10-06 14:44
//     */
//    @RequestMapping("/admin/bigfile/upload")
//    public void saveBigFile(String token,
//                            MultipartFile file,
//                            String path,
//                            Integer index,
//                            Integer total) {
//
//        //文件不能为空
//        if (file == null
//                || CheckUtil.isEmpty(path)
//                ) {
//            sendParamError();
//            return;
//        }
//
//        Integer flag = fileServ.saveUploadMap(token, index, total, file);
//        //如果 flag =1 表示全部切片接收完毕
//        if (flag == 1) {
//            //并发处理只能一个线程写盘
//            if (lockUtil.lockAccount(token)) {
//                FileIndex fi = fileServ.saveLocal(file.getOriginalFilename(), token, path);
//                sendSuccess(fi.getBaseJson());
//                //解除线程限制
//                lockUtil.unLockAccount(token);
//                return;
//            }
//        } else if (flag == 0) {
//            //表示当前切片上传成功
//            sendSuccess();
//            return;
//        } else if (flag == -1) {
//            sendJson(false, RespTips.DATA_NULL.code, RespTips.DATA_NULL.tips);
//            return;
//        }
//    }
//
//    /**
//     * 切片下载准备
//     *
//     * @param token
//     * @param id
//     * @author G/2018-10-06 16:25
//     */
//    @RequestMapping("/admin/bigfile/check")
//    public final void getDownloadCheck(String token, Long id) {
//
//        if (!CheckUtil.isLong(id)) {
//            sendParamError();
//            return;
//        }
//
//        FileIndex fileIndex = fileServ.findById(id);
//        if (fileIndex == null) {
//            sendJson(false, RespTips.DATA_NULL.code, RespTips.DATA_NULL.tips);
//            return;
//        }
//
//        File file = new File(fileIndex.getPath());
//        Integer eachSize = 1024 * 1024;
//        //加载文件切片
//        Integer total = fileServ.saveDownloadMap(token, file, eachSize);
//
//        if (total > 0) {
//            JSONObject jo = new JSONObject();
//            //总切片数量
//            jo.put("total", total);
//            //每片大小
//            jo.put("eachSize", eachSize);
//            jo.put("totalSize", file.length());
//            //文件id
//            jo.put("fileId", fileIndex.getId());
//            jo.put("fileName", file.getName());
//            sendSuccess(jo);
//        } else {
//            sendJson(false, RespTips.DATA_NULL.code, RespTips.DATA_NULL.tips);
//        }
//    }
//
//    /**
//     * 切片下载
//     *
//     * @param token
//     * @param index 切片id
//     * @author G/2018-08-18 16:25
//     */
//    @RequestMapping("/admin/bigfile/down")
//    public final void getCourseSplip(String token, Integer index) {
//
//        if (!CheckUtil.isInteger(index)) {
//            sendParamError();
//            return;
//        }
//
//        try {
//            byte[] slipByte = fileServ.getSplipByIndex(token, index);
//            sendFile("" + index, slipByte);
//        } catch (Exception e) {
//            sendJson(false, RespTips.DATA_NULL.code, RespTips.DATA_NULL.tips);
//        }
//    }
//
//    /**
//     * 下载完成后删除切片集合
//     * 考虑token失效后自动删除
//     *
//     * @param token
//     * @author G/2018-08-18 16:24
//     */
//    @RequestMapping("/admin/bigfile/remove")
//    public final void removeCourseSplip(String token) {
//        try {
//            fileServ.removeDownloadMap(token);
//            sendSuccess();
//        } catch (Exception e) {
//            sendJson(false, RespTips.DATA_NULL.code, RespTips.DATA_NULL.tips);
//        }
//    }
//
//    @RequestMapping("/clear/temp")
//    public final void clearTemp(String directory) {
//        if (CheckUtil.isEmpty(directory)) {
//            sendParamError();
//            return;
//        }
//
//        //获取存在临时向量中的token集合
//        Set<String> expireTokenSet = fileServ.getTempTokenSet(directory);
//        if (expireTokenSet != null && expireTokenSet.size() > 0) {
//            for (String token : expireTokenSet) {
//                //如果token已经失效
////                if(getActUser(token)==null){
////                    //删除对应的临时文件
////                    fileServ.clearTemp(directory,token);
////                }
//            }
//        }
////        log("clear temp==>"+expireTokenSet.size());
//    }
//
//}
