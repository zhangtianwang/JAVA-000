学习笔记

1 预备环境

 1 zookeeper 单服务器集群的安装

   见配置文件 zookeeper/zoo.cfg

   配置参数：
   dataDir=/data1/soft/zookeeper/zookeeper1/data
   dataLogDir=/data1/soft/zookeeper/zookeeper1/logs
   server.1=10.6.1.49:2287:3387
   server.2=10.6.1.49:2288:3388
   server.3=10.6.1.49:2289:3389
   clientPort=2181

   启动命令：./zkServer.sh start

2 activemq 单服务器集群的安装
  
   配置文件：activemq/activemq.xml 和 activemq/jetty.xml

3 kafka 集群安装
   配置文件见： kafka/server.perperties	



  