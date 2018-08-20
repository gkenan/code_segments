package com.personal.segments.rmi.client;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISumService extends Remote { // 必须继承Remote接口  
      
    // 需要远程调用的方法必须抛出RemoteException  
    public int sum(int a, int b) throws RemoteException;   
      
}  