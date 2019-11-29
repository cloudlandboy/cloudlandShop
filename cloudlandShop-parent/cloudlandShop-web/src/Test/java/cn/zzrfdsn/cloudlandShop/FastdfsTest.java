package cn.zzrfdsn.cloudlandShop;

import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * @Author cloudlandboy
 * @Date 2019/9/25 下午4:00
 * @Since 1.0.0
 */

public class FastdfsTest {

    @Test
    public void test1() throws Exception {
        // 1、加载配置文件(绝对路径)，配置文件中的内容就是tracker服务的地址。
        ClientGlobal.init("/home/cloudlandboy/workspace/idea01/cloudlandShop/cloudlandShop-parent/cloudlandShop-web/src/Test/resources/fastdfs.properties");
        // 2、创建一个TrackerClient对象。直接new一个。
        TrackerClient trackerClient=new TrackerClient();
        // 3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
        TrackerServer trackerServer = trackerClient.getConnection();
        // 4、创建一个StorageServer的引用，值为null
        StorageServer storageServer=null;
        // 5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
        StorageClient storageClient=new StorageClient(trackerServer,storageServer);
        // 6、使用StorageClient对象上传图片。
        //参数：图片路径，扩展名（不带点）,元数据(就是上传文件的详细信息，例如电脑右键查看详细信息)，返回上传成功后的文件储存路径
        String[] jpegs = storageClient.upload_file("/home/cloudlandboy/Pictures/bg/2019009232217.jpeg", "jpeg",null);

        for (String jpeg : jpegs) {
            System.out.println(jpeg);
        }
    }
}