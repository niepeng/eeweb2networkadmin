


-- t_equip_data 单个程序的时候表


CREATE TABLE `t_admin_loginaccount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vcLoginName` char(13) COLLATE utf8_bin DEFAULT NULL COMMENT '系统的后台登陆账号，默认采用手机号码。',
  `vcLoginPassword` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `vcRealName` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `vcPhone` char(13) COLLATE utf8_bin DEFAULT NULL,
  `iValid` int(2) DEFAULT '1' COMMENT '是否有效。\\r\\n            1 -- 有效 （默认）\\r\\n            2 -- 无效  3--暂时锁定',
  `lockTime` datetime DEFAULT NULL COMMENT '最近锁定时间',
  `dtCreate` datetime DEFAULT NULL COMMENT '该条记录第一次创建的时间。',
  `dtModify` datetime DEFAULT NULL COMMENT '最后一次修改时间，每次修改需更改此字段',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统管理平台登陆账号表';


CREATE TABLE `t_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `param_code` varchar(255) DEFAULT NULL COMMENT '参数编号',
  `param_value` text COMMENT '参数值',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统参数配置';


CREATE TABLE `t_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `sms_phones` varchar(1024) DEFAULT NULL COMMENT '关联短信报警的手机号码,逗号分隔',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='区域表';


CREATE TABLE `t_sms_phone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `phone` varchar(64) DEFAULT NULL COMMENT '手机号',
  `name` varchar(64) DEFAULT NULL COMMENT '姓名',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='短信关联人表';


CREATE TABLE `t_device_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `area_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '所属区域',
  `sn` varchar(64) DEFAULT NULL COMMENT 'sn号',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `tag` varchar(128) DEFAULT NULL COMMENT '标签',
  `address` int(11) not NULL  DEFAULT 0 COMMENT '设备地址',
  `type` int(11) not NULL  DEFAULT 0 COMMENT '设备类型:按位存储',

  `temp_up` int(11) not NULL  DEFAULT 0 COMMENT '温度上限:23.34,存储2334',
  `temp_down` int(11) not NULL  DEFAULT 0 COMMENT '温度下限:23.34,存储2334',
  `temp_dev` int(11) not NULL  DEFAULT 0 COMMENT '温度校正值:23.34,存储2334',

  `humi_up` int(11) not NULL  DEFAULT 0 COMMENT '湿度上限:45.67,存储4567',
  `humi_down` int(11) not NULL  DEFAULT 0 COMMENT '湿度下限:45.67,存储4567',
  `humi_dev` int(11) not NULL  DEFAULT 0 COMMENT '湿度校正值:45.67,存储4567',

  `shine_up` int(11) not NULL  DEFAULT 0 COMMENT '光照上限',
  `shine_down` int(11) not NULL  DEFAULT 0 COMMENT '光照下限',
  `shine_dev` int(11) not NULL  DEFAULT 0 COMMENT '光照校正值',

  `pressure_up` int(11) not NULL  DEFAULT 0 COMMENT '压力上限',
  `pressure_down` int(11) not NULL  DEFAULT 0 COMMENT '压力下限',
  `pressure_dev` int(11) not NULL  DEFAULT 0 COMMENT '压力校正值',

  `control_way` smallint(4) unsigned not NULL  DEFAULT 0 COMMENT '只有开关量输出才有值:控制通道',
  `relation_out_id` bigint(20) not NULL  DEFAULT 0 COMMENT '只有开关量输入才有值:关联对应的开关量输出的id(本表的id)',

  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='设备信息表';


CREATE TABLE `t_out_condition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_info_id` bigint(20) NOT NULL default 0 COMMENT '关联设备信息表id',
  `open_closed` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '打开关闭条件类型:1打开,2关闭',
  `device_type` int(11) not NULL  DEFAULT 0 COMMENT '条件设备类型:温度,湿度,光照等',
  `device_address` int(11) not NULL  DEFAULT 0 COMMENT '条件设备地址',
  `min_max` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '小于大于:1小于,2大于',
  `data_value` int(11) not NULL  DEFAULT 0 COMMENT '数值,参考t_device_info表中的值存储格式',

  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='开关量输出条件表';



--  温度 + 湿度 + 电量 + 光照 + 压力 +  烟感(开关量输入) 浸水(开关量输入) +停电来电(开关量输入) +  开关量输出(报警器)0没有输出,1有输出
CREATE TABLE `t_device_data_intime` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备id',
  `status` int(11) not NULL  DEFAULT 0 COMMENT '状态:1正常,2报警,3离线',
  `temp` int(11) not NULL  DEFAULT 0 COMMENT '温度:23.34,存储2334',
  `humi` int(11) not NULL  DEFAULT 0 COMMENT '湿度:45.67,存储4567',
  `power` int(11) not NULL  DEFAULT 0 COMMENT '电量:300,代表3v',
  `shine` int(11) not NULL  DEFAULT 0 COMMENT '光照:20000,存储直接值',
  `pressure` int(11) not NULL  DEFAULT 0 COMMENT '压力:10000,存储实际值',
  `smoke` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '烟感:0没有报警,1报警',
  `water` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '浸水:0没有报警,1报警',
  `electric` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '停电来电:0停电,1来电',
  `out` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '开关量输出(报警器)0没有输出,1有输出',

  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='设备实时数据表';



CREATE TABLE `t_device_alarm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备id',
  `alarm_type` int(11) not NULL  DEFAULT 0 COMMENT '报警类型按照位存储: 温度 + 湿度 + 电量 + 光照 + 压力 +  烟感(开关量输入) + 浸水(开关量输入)  +停电来电(开关量输入) +  开关量输出(报警器):0低,1高或者不区分;',
  `status` int(11) not NULL  DEFAULT 0 COMMENT '状态:1系统报警,2系统确认,3人工确认',
  `temp` int(11) not NULL  DEFAULT 0 COMMENT '温度:23.34,存储2334',
  `humi` int(11) not NULL  DEFAULT 0 COMMENT '湿度:45.67,存储4567',
  `power` int(11) not NULL  DEFAULT 0 COMMENT '电量:300,代表3v',
  `shine` int(11) not NULL  DEFAULT 0 COMMENT '光照:20000,存储直接值',
  `pressure` int(11) not NULL  DEFAULT 0 COMMENT '压力:10000,存储实际值',
  `smoke` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '烟感:0没有报警,1报警',
  `water` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '浸水:0没有报警,1报警',
  `electric` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '停电来电:0停电,1来电',
  `out` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '开关量输出(报警器)0没有输出,1有输出',
  `recently_alarm_time` datetime DEFAULT NULL COMMENT '最近一次报警时间',
  `system_confirm_time` datetime DEFAULT NULL COMMENT '系统确认时间',
  `user_confirm_time` datetime DEFAULT NULL COMMENT '人工确认时间',

  `temp_normal` varchar(128)  COMMENT '温度正常范围',
  `humi_normal` varchar(128)  COMMENT '温度正常范围',
  `power_normal` varchar(128)  COMMENT '电压正常范围',
  `shine_normal` varchar(128)  COMMENT '光照正常范围',
  `pressure_normal` varchar(128)  COMMENT '压力正常范围',

  `note` varchar(256) DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='报警数据表';


CREATE TABLE `t_device_data_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备id',
  `status` int(11) not NULL  DEFAULT 0 COMMENT '状态:1正常,2报警,3离线',
  `temp` int(11) not NULL  DEFAULT 0 COMMENT '温度:23.34,存储2334',
  `humi` int(11) not NULL  DEFAULT 0 COMMENT '湿度:45.67,存储4567',
  `power` int(11) not NULL  DEFAULT 0 COMMENT '电量:300,代表3v',
  `shine` int(11) not NULL  DEFAULT 0 COMMENT '光照:20000,存储直接值',
  `pressure` int(11) not NULL  DEFAULT 0 COMMENT '压力:10000,存储实际值',
  `smoke` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '烟感:0没有报警,1报警',
  `water` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '浸水:0没有报警,1报警',
  `electric` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '停电来电:0停电,1来电',
  `out` smallint(2) unsigned not NULL  DEFAULT 0 COMMENT '开关量输出(报警器)0没有输出,1有输出',

  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '修改时间',
  `created_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='设备历史数据表';
