package cn.zzrfdsn.cloudlandShop.util;

import org.junit.Test;

public class FastDFSClientUtilsTest {

    @Test
    public void test() throws Exception {
        //参数：配置文件路径
        FastDFSClientUtils fastDFSClientUtils=new FastDFSClientUtils("classpath:fastdfs.properties");

        String path = fastDFSClientUtils.uploadFile("/home/cloudlandboy/Pictures/Overlooking_by_Lance_Asper.jpg", "jpg", null);

        System.out.println(path);
    }
}