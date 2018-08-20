package com.personal.segments.rmi.server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.personal.segments.rmi.client.ISumService;

/** 
 * 远程接口的实现类 
 * @author zhangjim 
 */  
public class SumServiceImpl extends UnicastRemoteObject implements ISumService { // 必须从UnicastRemoteObject继承   
  
	private static final long serialVersionUID = 1495979325800023992L;

	// 需要一个抛出Remote异常的默认初始化方法   
    SumServiceImpl() throws RemoteException {   
          
    }  
      
    // 业务方法, 传入两个数字, 返回相加结果  
    public int sum(int a, int b) throws RemoteException {  
        return a + b;  
    }  
}  