package com.personal.segments.rmi.server;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import com.personal.segments.rmi.client.ISumService;

public class Server {  
    public static void main(String[] args) {  
        try {  
            // 创建一个远程对象   
            ISumService sumService = new SumServiceImpl();  
              
            // 创建RMI注册表, 启动RMI服务, 并指定端口为8888(Java默认端口是1099)  
            // 这一步必不可少, 缺少注册表创建,则无法绑定对象到远程注册表上   
            LocateRegistry.createRegistry(8888);   
              
            // 把远程对象注册到RMI注册服务器上，并命名为sum   
            // 绑定的URL标准格式为：rmi://host:port/name(其中协议名可以省略,
            // 多主机时不能绑定localhost，需要指定相应网卡地址，否则会连不上，
            // 复杂对象可能需要修改java安全策略
            Naming.bind("rmi://localhost:8888/sum", sumService);  
            System.out.println("远程对象绑定成功！");  
        } catch (Exception e) {  
            e.printStackTrace(); 
        }  
    }  
}  