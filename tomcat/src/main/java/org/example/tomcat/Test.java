package org.example.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;


/**
 * @author laylee
 */

public class Test {
	public static void main(String[] args) {
		Tomcat tom = new Tomcat();
		tom.setPort( 8080 );
		try {
			//获取项目编译后的classes 路径
			String path = Test.class.getResource( "/" ).getPath();
			//获取webapp 文件
			String filePath = new File( "src/main/webapp" ).getAbsolutePath();
			//然后将webapp下的项目添加至tomcat的context容器（context对应一个运行的项目）
			Context context = tom.addWebapp( "/", filePath );
			// 参数1：一般是项目名 对应请求url中的项目名
			//webResourceRoot 用于加载 项目的class文件
			WebResourceRoot webResource = new StandardRoot( context );
			webResource.addPreResources( new DirResourceSet( webResource, "/WEB-INF/classes", path, "/" ) );
			// tomcat启动
			tom.start(); //阻塞 ，等待前端连接
			tom.getServer().await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

