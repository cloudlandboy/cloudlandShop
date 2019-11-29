package cn.zzrfdsn.cloudlandShop.controller;

import cn.zzrfdsn.cloudlandShop.util.FastDFSClientUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author cloudlandboy
 * @Date 2019/9/25 下午9:03
 * @Since 1.0.0
 */
@Controller
public class PictureController {

    @Value("${IMG_SERVER_URL}")
    private String IMG_SERVER_URL;

    //指定Content-Type和字符集
    @RequestMapping(value = "/pic/upload",produces = MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    /**
     * public Map<String, Object> upload(MultipartFile uploadFile) {
     */
    public String upload(MultipartFile uploadFile) throws JsonProcessingException {
        Map<String, Object> data=new HashMap<>(2);
        //截取文件后缀名
        String fileName = uploadFile.getOriginalFilename();
        try {
            String subfix = fileName.substring(fileName.lastIndexOf(".") + 1);

            //上传图片
            FastDFSClientUtils fastDFSClientUtils = new FastDFSClientUtils("classpath:fastdfs.properties");
            String savePath = fastDFSClientUtils.uploadFile(uploadFile.getBytes(), subfix);

            //拼接路径
            savePath = IMG_SERVER_URL + savePath;
            //成功后的数据格式
            data.put("error", 0);
            data.put("url", savePath);
            System.out.println(savePath);
        } catch (Exception e) {
            //失败后的数据格式
            data.put("error", 1);
            data.put("message", fileName + " 上传失败");
        }
        //为了解决浏览器兼容性，直接返回json字符串，这样返回的Content-Type就是text/plain
        return new ObjectMapper().writeValueAsString(data);
    }
}