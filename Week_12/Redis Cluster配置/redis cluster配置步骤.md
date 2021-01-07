

1 创建模板的配置文件 redis-cluster.tmpl



2 根据配置文件创建redis集群各个节点的目录和配置文件，并将不同的端口替换掉模板配置文件中端口变量

for port in `seq 6371 6376`; do \
  mkdir -p ${port}/conf \
  && PORT=${port} envsubst < redis-cluster.tmpl > ${port}/conf/redis.conf \
  && mkdir -p ${port}/data;\
done


3 根据镜像批量创建不同端口的redis docker容器

for port in $(seq 6371 6376); do \
  docker run -di --restart always --name redis-${port} --net host \
  -v /data1/docker/redis-cluster/${port}/conf/redis.conf:/usr/local/etc/redis/redis.conf \
  -v /data1/docker/redis-cluster/${port}/data:/data \
  redis redis-server /usr/local/etc/redis/redis.conf; \
done


4 将多个节点搭建成redis cluster

 redis-cli -a 1234 --cluster create 10.6.1.49:6371 10.6.1.49:6372 10.6.1.49:6373 10.6.1.49:6374 10.6.1.49:6375 10.6.1.49:6376 --cluster-replicas 1

 5 检查集群的状态

 redis-cli -a 1234 --cluster check 10.6.1.49:6371

 6 连接上集群进行redis操作

  docker exec -it redis-6371 /usr/local/bin/redis-cli -c -a 1234 -h 10.6.1.49 -p 6371
   