package cn.zzrfdsn.cloudlandShop.item.message.listenter;

import cn.zzrfdsn.cloudlandShop.item.vo.ItemInfo;
import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.pojo.TbItemDesc;
import cn.zzrfdsn.cloudlandShop.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author cloudlandboy
 * @Date 2019/10/20 下午4:00
 * @Since 1.0.0
 */

public class ItemChangeListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Value("${item_html_path}")
    private String html_path;

    @Override

    public void onMessage(Message message) {
        logger.debug("收到添加商品的消息，开始生成静态页面");
        try {
            TextMessage textMessage = (TextMessage) message;
            long itemId = Long.parseLong(textMessage.getText());

            //查询商品信息
            TbItem item = itemService.findItemById(itemId);
            TbItemDesc itemDesc = itemService.findItemDescById(itemId);

            ItemInfo itemInfo = new ItemInfo();
            BeanUtils.copyProperties(item, itemInfo);

            //封装数据
            Map<String, Object> data = new HashMap<>();
            data.put("item", itemInfo);
            data.put("itemDesc", itemDesc);

            //获取模板
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");

            //写出到静态文件存放路径
            Writer writer = new BufferedWriter(new FileWriter(html_path + File.separator + itemId + ".html"), 1024);
            template.process(data, writer);

            writer.close();

            logger.debug("创建商品静态页面成功！");
        } catch (Exception e) {
            logger.error("创建商品静态页面出错！", e);
        }
    }
}