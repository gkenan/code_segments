package com.personal.segments.rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * 客户端测试，在客户端调用远程对象上的远程方法，并返回结果
 * 
 */
public class Client {
	public static void main(String[] args) {

		// 调用相加方法
		try {
			// 在RMI服务注册表中查找名称为sum的对象
			ISumService sumService = (ISumService) Naming.lookup("rmi://localhost:8888/sum");
			System.out.println("相加结果为: " + sumService.sum(3, 2));
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

	}
}