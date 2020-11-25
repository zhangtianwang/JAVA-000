

CREATE TABLE `customer` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '客户姓名',
  `gender` INT(11) NOT NULL DEFAULT '0' COMMENT '0未知，1男，2女',
  `mobile` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '手机号',
  `career` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '职业',
  `education` INT(11) NOT NULL DEFAULT '0' COMMENT '0未知，1小学，2初中，3高中，4专科，5本科，6研究生，7博士',
  `age` INT(11) NOT NULL DEFAULT '0' COMMENT '年龄',
  `marital` INT(11) NOT NULL DEFAULT '1' COMMENT '0未知，1已婚，2未婚',
  `status` INT(11) NOT NULL DEFAULT '1' COMMENT '1使用，2禁用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `mobile` (`mobile`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `customer_detail` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `customer_id` INT(11) NOT NULL DEFAULT '0' COMMENT '客户id',
  `level` INT(11) NOT NULL DEFAULT '0' COMMENT '客户等级，0年消费1万以内，1 年消费10W以内，2年消费10W+',
  `province_id` INT(11) NOT NULL DEFAULT '0' COMMENT '所在省份id',
  `city_id` INT(11) NOT NULL DEFAULT '0' COMMENT '所在城市id',
  `county_id` INT(11) NOT NULL DEFAULT '0' COMMENT '所在区县id',
  `address` VARCHAR(500) NOT NULL DEFAULT '' COMMENT '客户常用的详细地址',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `mobile` (`customer_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `product` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type_id` INT(11) NOT NULL DEFAULT '0' COMMENT '产品类型id，对应product_type的id',
  `price` DECIMAL(18,2) NOT NULL DEFAULT '0' COMMENT '价格,单位元',
  `stock` INT(11) NOT NULL DEFAULT '0' COMMENT '库存量',
  `start_date` VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '商品售卖开始日期，yyyy-MM-dd',
  `end_date` VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '商品售卖结束日期，yyyy-MM-dd',
  `remark` VARCHAR(200) NOT NULL DEFAULT '0' COMMENT '商品描述',
  `sale_status` INT(11) NOT NULL DEFAULT '0' COMMENT '0待发布，1售卖中，2已停售',
  `creator_id` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建人id',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `type_id` (`type_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8  COMMENT='产品表';

CREATE TABLE `product_attribute` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '商品属性名',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='产品属性表';

CREATE TABLE `product_attribute_relation` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `product_id` INT(11) NOT NULL DEFAULT '0' COMMENT '商品id',
  `product_attr_id` INT(11) NOT NULL DEFAULT '0' COMMENT '商品属性id',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `product_attr_id` (`product_attr_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='产品和属性关联表，可以根据属性对产品进行筛选';

CREATE TABLE `product_snap` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `product_id` INT(11) NOT NULL DEFAULT '0' COMMENT '产品id',
  `price` DECIMAL(18,2) NOT NULL DEFAULT '0' COMMENT '价格,单位元',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='产品快照表，订单生成时记录上';


CREATE TABLE `product_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL DEFAULT '0' COMMENT '品类名称',
  `level` INT(11) NOT NULL DEFAULT '0' COMMENT '产品层级',
  `parent_id` INT(11) NOT NULL DEFAULT '0' COMMENT '父级id，对应product_type表的id，最高级是0',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8  COMMENT='产品类型表';

CREATE TABLE `order` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `order_id` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '订单id',
  `status` INT(11) NOT NULL DEFAULT '0' COMMENT '订单状态，0待付款，1已付款，2已收货，3退款中，4退款完成，5已完成',
  `deal_price` DECIMAL(18,2) NOT NULL DEFAULT '0' COMMENT '成交价',
  `pay_time` DATETIME NULL COMMENT '支付时间',
  `deal_time` DATETIME NULL COMMENT '完成时间',
  `customer_id` INT(11) NOT NULL DEFAULT '0' COMMENT '客户id',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8  COMMENT='订单表'; 

CREATE TABLE `order_detail` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `order_id` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '订单id',
  `province_id` INT(11) NOT NULL DEFAULT '0' COMMENT '收货省份id',
  `city_id` INT(11) NOT NULL DEFAULT '0' COMMENT '收货城市id',
  `county_id` INT(11) NOT NULL DEFAULT '0' COMMENT '区县id',
  `address` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '详细地址',
  `remark` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '买家备注信息',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8  COMMENT='订单明细表'; 

CREATE TABLE `order_product` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `order_id` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '订单id',
  `snap_id` INT(11) NOT NULL DEFAULT '0' COMMENT '商品快照id',
  `count` INT(11) NOT NULL DEFAULT '0' COMMENT '购买的商品的数量',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `snap_id` (`snap_id`),
  KEY `order_id` (`order_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='订单关联产品表'; 

