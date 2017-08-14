package cn.e3mall.fastdfs;


import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastDFSTest {
	
	@Test
	public void testName() throws Exception {
		// 1、加载配置文件，配置文件中的内容就是tracker服务的地址。
		ClientGlobal.init("D:/JavaProject/project3/e3-manager-web/src/main/resources/conf/client.conf");
		// 2、创建一个TrackerClient对象。直接new一个。
		TrackerClient trackerClient=new TrackerClient();
		// 3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
		TrackerServer server = trackerClient.getConnection();
		// 4、创建一个StorageServer的引用，值为null
		StorageServer storageServer=null;
		// 5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
		StorageClient storageClient=new StorageClient(server, storageServer);
		// 6、使用StorageClient对象上传图片。
		//扩展名不带“.”
		String[] strings = storageClient.upload_file("C:/Users/吾霞/Desktop/01.参考资料/广告图片/9a80e2d06170b6bb01046233ede701b3.jpg", "jpg", null);
	
		//7、返回数组。包含组名和图片的路径。
		for (String string : strings) {
			System.out.println(string);
		}
	}
}
