# 一：nacos简介及安装 #

----------


	# nacos简介 #
	
	Nacos 致力于发现、配置和管理微服务,提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。


    # nacos的安装 # 
	    下载nacos-server-1.1.4.zip，下载之后，解压，然后双击startup.cmd，
    	启动成功。
		然后访问： http://127.0.0.1:8848/nacos/index.html，进入到nacos的管理后台。
	

----------
	

# 二：soul如何集成nacos进行数据同步 #

----------

	

 soul-admin和soul-bootstrap配置文件引入nacos的配置
    
	 nacos:
      url: localhost:8848
      namespace: 1c10d748-af86-43b9-8265-75f487d20c6c
      acm:
        enabled: false
        endpoint: acm.aliyun.com
        namespace:
        accessKey:
        secretKey:

  soul-admin和soul-bootstrap项目引入依赖包
    
	 <dependency>
            <groupId>com.alibaba.nacos</groupId>
            <artifactId>nacos-client</artifactId>
            <version>${nacos-client.version}</version>
        </dependency>
 
  分别启动soul-admin，soul-bootstrap，项目启动成功。
  
	

----------

# 三：nacos数据同步流程源码分析 #

----------
 
> 大体的流程：1 soul-admin启动——》全量推送配置数据到zookeeper。  
> 		    2 soul-bootstrap启动——》从zookeeper拉取数据到读取到soul-bootstrapd的内存。  
> 		    3 客户调用——》请求到soul-bootstrap，boot-strap从内存中获取到配置数据
> 		      
 # soul-admin项目推送数据到zookeeper： 
		
		1 首先定位到NacosSyncDataService注入到Spring容器的SynDataService的Bean.
		 @Bean
    public SyncDataService nacosSyncDataService(final ObjectProvider<ConfigServiceconfigService, final ObjectProvider<PluginDataSubscriberpluginSubscriber,
                                           final ObjectProvider<List<MetaDataSubscriber>metaSubscribers, final ObjectProvider<List<AuthDataSubscriber>authSubscribers) {
        log.info("you use nacos sync soul data.......");
        return new NacosSyncDataService(configService.getIfAvailable(), pluginSubscriber.getIfAvailable(),
                metaSubscribers.getIfAvailable(Collections::emptyList), authSubscribers.getIfAvailable(Collections::emptyList));
    }

----------

	 2 我们进一步进入NacosSyncDataService的构造方法，内部执行了start方法

	 public NacosSyncDataService(final ConfigService configService, final PluginDataSubscriber pluginDataSubscriber,
                                final List<MetaDataSubscriber> metaDataSubscribers, final List<AuthDataSubscriber> authDataSubscribers) {
        start();
    }
    public void start() {
        watcherData(PLUGIN_DATA_ID, this::updatePluginMap);
        watcherData(SELECTOR_DATA_ID, this::updateSelectorMap);
        watcherData(RULE_DATA_ID, this::updateRuleMap);
        watcherData(META_DATA_ID, this::updateMetaDataMap);
        watcherData(AUTH_DATA_ID, this::updateAuthMap);
    }

----------
	3 我们
   	
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
        List<PluginData> pluginDataList = pluginService.listAll();
        eventPublisher.publishEvent(new DataChangedEvent(ConfigGroupEnum.PLUGIN, type, pluginDataList));
        ...
    }

	eventPublisher执行了publishEvent，定位到DataChangedEventDispatcher实现了ApplicationListener，用onApplicationEvent监听到事件，进行处理
	

	public void onApplicationEvent(final DataChangedEvent event) {
        for (DataChangedListener listener : listeners) {
            switch (event.getGroupKey()) {
                case APP_AUTH:
                    listener.onAppAuthChanged((List<AppAuthData>) event.getSource(), event.getEventType());
                    break;
               ......
            }
        }
    }
	
	我们开始了Zookeeper的配置，从容器中拿到listener是ZookeeperDataChangedListener，
	进入到这个类，这个是核心类，获取到zk的path，往zk里去同步数据。
	 public void onPluginChanged(final List<PluginData> changed, final DataEventTypeEnum eventType) {
        for (PluginData data : changed) {
			
            final String pluginPath = ZkPathConstants.buildPluginPat
            upsertZkNode(pluginPath, data);
			....
        }
    }

	至此，soul-admin里面往zk同步数据代码就到这里

----------
	
	
# soul-bootsrap从zookeeper拉取配置信息加载到缓存
	
> soul-bootstrap项目引入soul-spring-boot-starter-sync-data-zookeeper，我们找到这个项目的源码，找到ZookeeperSyncDataConfiguration这个核心配置类，

    @Bean
    public SyncDataService syncDataService(final ObjectProvider<ZkClientzkClient, final ObjectProvider<PluginDataSubscriberpluginSubscriber,
       final ObjectProvider<List<MetaDataSubscriber>metaSubscribers, final ObjectProvider<List<AuthDataSubscriber>authSubscribers) {
    log.info("you use zookeeper sync soul data.......");    
    return new ZookeeperSyncDataService(zkClient.getIfAvailable(),    pluginSubscriber.getIfAvailable(),
    metaSubscribers.getIfAvailable(Collections::emptyList), authSubscribers.getIfAvailable(Collections::emptyList));
    }
    

----------
	
从日志可以看出这是开始用zookeeper同步数据。进入到实例话方法：


     public ZookeeperSyncDataService(final ZkClient zkClient, final PluginDataSubscriber pluginDataSubscriber,
                                    final List<MetaDataSubscriber> metaDataSubscribers, final List<AuthDataSubscriber> authDataSubscribers) {
        this.zkClient = zkClient;
        this.pluginDataSubscriber = pluginDataSubscriber;
        this.metaDataSubscribers = metaDataSubscribers;
        this.authDataSubscribers = authDataSubscribers;
        watcherData();
        watchAppAuth();
        watchMetaData();
    }

----------
	然后我们进入watcherData方法内部
	 private void watcherData() {
        final String pluginParent = ZkPathConstants.PLUGIN_PARENT;
        List<String> pluginZKs = zkClientGetChildren(pluginParent);
        for (String pluginName : pluginZKs) {
			//获取到数据，将数据写入缓存
            watcherAll(pluginName);
        }
        zkClient.subscribeChildChanges(pluginParent, (parentPath, currentChildren) -> {
            if (CollectionUtils.isNotEmpty(currentChildren)) {
                for (String pluginName : currentChildren) {
                    watcherAll(pluginName);
                }
            }
        });
    }
	进入到watchAll方法：
	private void watcherAll(final String pluginName) {
        watcherPlugin(pluginName);
        watcherSelector(pluginName);
        watcherRule(pluginName);
    }
	然后进入到一种的监控plugin方法，这个是核心的方法，
	private void watcherPlugin(final String pluginName) {
        String pluginPath = ZkPathConstants.buildPluginPath(pluginName);
        if (!zkClient.exists(pluginPath)) {
            zkClient.createPersistent(pluginPath, true);
        }
		//写入缓存数据
        cachePluginData(zkClient.readData(pluginPath));
        subscribePluginDataChanges(pluginPath, pluginName);
    }
	
	//可以看到把数据写入了本地的缓存PLUGIN_MAP，是一个Map。
	public void cachePluginData(final PluginData pluginData) {
        Optional.ofNullable(pluginData).ifPresent(data -> PLUGIN_MAP.put(data.getName(), data));
    }
	

----------

	//接下来我们看下接口调用soul-bootstrap网关，调用到AbstractPlugin, 从缓存中取数据，取不到的话，直接返回

	public Mono<Void> execute(final ServerWebExchange exchange, final SoulPluginChain chain) {
        String pluginName = named();
		//从缓存中取数据，不做处理直接返回。
        final PluginData pluginData = BaseDataCache.getInstance().obtainPluginData(pluginName);
        if (pluginData != null && pluginData.getEnabled()) {
            final Collection<SelectorData> selectors = BaseDataCache.getInstance().obtainSelectorData(pluginName);
            if (CollectionUtils.isEmpty(selectors)) {
                return handleSelectorIsNull(pluginName, exchange, chain);
            }
     
----------

	

	从源码的跟踪，追溯了初始化soul-admin怎么把数据写入到zookeeper，soul-bootstrap项目启动
	从zookeeper从读取数据，加载到自己内存的过程。
	soul-admin增删改配置数据之后，soul-bootstrap数据及时生效的数据同步，也基本一致，这里我们不再赘述。
	
    




	

	


   
	


