

1 根据镜像创建容器

docker run -p 26360:26360 -it --name redis-sentinel1 -v /data1/docker/redis-sentinal/sentinel1.conf:/etc/redis/sentinel.conf  -d redis /bin/bash

2 打开交互模式的终端

docker exec -it redis-sentinel1 bash


3 执行sentinel命令

redis-sentinel /etc/redis/sentinel.conf

4 可以用redis-cli登录 info 查看运行状态