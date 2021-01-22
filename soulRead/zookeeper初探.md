# 一：zookeeper简介及安装 #

----------

	# zookeeper简介 #
   zookeper的[官方文档](https://zookeeper.apache.org/ "官方文档地址")对zookeeper的
功能做了描述：zookeeper是一个分布式的协调服务，提供配置中心,域名命名，分布式同步，组服务等，在分布式系统中，微服务很多，服务管理比较困难，比如服务节点的故障，服务节点的新增，
zookeeper来管理实现服务的高可用。	

    # zookeeper的安装 # 
	    zookeeper的安装也比较简单，找一个最新的版本的zookeeper的包，比如下载zookeeper-release-3.6.2.tar，下载之后，找到bin下面的zkServer.cmd，然后双击，
    	zookeeper启动成功。
	

----------
	

# 二：soul集成zookeeper进行数据同步 #

----------

	

 soul-admin项目配置文件application.yml，关掉websocket的默认配置，开启zookeeper的配置
    
 > 	soul:  
> 	  sync:  
>         zookeeper:
>           url: localhost:2181  
>           sessionTimeout: 5000  
>           connectionTimeout: 2000

  soul-bootstrap项目引入依赖包
	>  <dependency>
>       <groupId>org.dromara</groupId>
>        <artifactId>soul-spring-boot-starter-sync-data-zookeeper</artifactId>
>        <version></version>
>      </dependency>
 
  
  soul-bootstrap项目引入依赖包    
	 
> 	soul:  
> 	  sync:  
>         zookeeper:
>           url: localhost:2181  
>           sessionTimeout: 5000  
>           connectionTimeout: 2000
 

启动soul-admin，启动成功
启动soul-bootstrap，启动成功。

 

----------

# 三：zookeeper数据同步流程源码分析 #

----------
 
> 大体的流程：1 soul-admin启动——》全量推送配置数据到zookeeper。  
> 		    2 soul-bootstrap启动——》从zookeeper拉取数据到读取到soul-bootstrapd的内存。  
> 		    3 客户调用——》请求到soul-bootstrap，boot-strap从内存中获取到配置数据
> 		      
接下来，我们通过源码验证下这个流程：
		
		1 首先我们找到这个注入DataSyncConfiguration类下注入ZookeeperDataInit的Bean的方法，发现依赖于先把ZkClient和DataSyncConfiguration注入到容器。
		@Bean
        @ConditionalOnMissingBean(ZookeeperDataInit.class)
        public ZookeeperDataInit zookeeperDataInit(final ZkClient zkClient, final SyncDataService syncDataService) {
            return new ZookeeperDataInit(zkClient, syncDataService);
        }

		2 通过查找引用的方式，我们找到了ZookeeperConfiguration类的注入ZkClient的方法，因为配置文件中有zookeeper的配置，所以ZookeeperProperties已经被注入到容器，ZK也可注入到容器

	@Bean
    @ConditionalOnMissingBean(ZkClient.class)
    public ZkClient zkClient(final ZookeeperProperties zookeeperProp) {
        return new ZkClient(zookeeperProp.getUrl(), zookeeperProp.getSessionTimeout(), zookeeperProp.getConnectionTimeout());
    }
   	
	     3 我们再找Zookeeper依赖注入的SyncDataService，猜测是不是有Zookeeper实现的SyncDataService，注入到容器了，果然我们找到了ZookeeperSyncDataService，
		 @Bean
    public SyncDataService syncDataService(final ObjectProvider<ZkClient> zkClient, final ObjectProvider<PluginDataSubscriber> pluginSubscriber,
                                           final ObjectProvider<List<MetaDataSubscriber>> metaSubscribers, final ObjectProvider<List<AuthDataSubscriber>> authSubscribers) {
        return new ZookeeperSyncDataService(zkClient.getIfAvailable(), pluginSubscriber.getIfAvailable(),
                metaSubscribers.getIfAvailable(Collections::emptyList), authSubscribers.getIfAvailable(Collections::emptyList));
     }
		4 ZookeeperDataInit已经初始化完成，接下来我们看下如何同步的数据，ZookeeperDataInit方法实现了CommondLineRunner,spring容器初始化中，会执行run方法，即同步数据的方法。
	> public class ZookeeperDataInit implements CommandLineRunner
	 @Override
    public void run(final String... args) {
        String pluginPath = ZkPathConstants.PLUGIN_PARENT;
        String authPath = ZkPathConstants.APP_AUTH_PARENT;
        String metaDataPath = ZkPathConstants.META_DATA;
        if (!zkClient.exists(pluginPath) && !zkClient.exists(authPath) && !zkClient.exists(metaDataPath)) {
            syncDataService.syncAll(DataEventTypeEnum.REFRESH);
        }
    }
		
	 我们再进一步看下synAll的实现方法：
	@Override
    public boolean syncAll(final DataEventTypeEnum type) {
        appAuthService.syncData();
        List<PluginData> pluginDataList = pluginService.listAll();
        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.PLUGIN, type, pluginDataList));
        List<SelectorData> selectorDataList = selectorService.listAll();
        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.SELECTOR, type, selectorDataList));
        List<RuleData> ruleDataList = ruleService.listAll();
        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.RULE, type, ruleDataList));
        metaDataService.syncData();
        return true;
    }

	通过ApplicationEventPublisher发布订阅，把消息发出去。
	zk的内容较多，今天先到这里
	  
	


   
	


