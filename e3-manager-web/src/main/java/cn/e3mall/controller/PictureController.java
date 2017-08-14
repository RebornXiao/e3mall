package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

/**
 * 图片控制器
 * @author 吾霞
 *
 */
@Controller
public class PictureController {
	
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String fileUpload(MultipartFile uploadFile){
		try {
			//1、取文件的扩展名
			String filename = uploadFile.getOriginalFilename();
			String ext = filename.substring(filename.lastIndexOf(".")+1);
			//2、创建一个FastDFS的客户端
			FastDFSClient fastDFSClient=new FastDFSClient("classpath:conf/client.conf");
			//3、执行上传处理
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(),ext);
			//4、拼接返回的url和ip地址，拼装成完整的url
			String url=IMAGE_SERVER_URL+path;
			
			Map result=new HashMap<>();
			result.put("error",0);
			result.put("url",url);
			String json = JsonUtils.objectToJson(result);
			return json;
		} catch (Exception e) {
			Map result=new HashMap<>();
			result.put("error",1);
			result.put("message","图片上传失败");
			String json = JsonUtils.objectToJson(result);
			return json;
		}
	}
}
