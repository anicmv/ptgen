DROP TABLE IF EXISTS `dou_ban`;
CREATE TABLE `dou_ban`
(
    `id`                   bigint        NOT NULL COMMENT '豆瓣id',
    `type`                 varchar(32)   NOT NULL COMMENT '豆瓣类型 TVSeries or Movie',
    `title`                varchar(512)  NOT NULL COMMENT '标题',
    `original_title`       varchar(1024) NOT NULL COMMENT '片名',
    `translated_name`      varchar(1024) DEFAULT NULL COMMENT '译名',
    `year`                 int           NOT NULL COMMENT '年代',
    `countries`            varchar(30)   DEFAULT NULL COMMENT '制片国家/地区/产地',
    `official_website`     varchar(1024) DEFAULT NULL COMMENT '官方网站',
    `main_pic`             varchar(1024) DEFAULT NULL COMMENT '海报链接',
    `genres`               varchar(1024) DEFAULT NULL COMMENT '类型/类别',
    `languages`            varchar(1024) DEFAULT NULL COMMENT '语言',
    `publish_date`         varchar(1024) DEFAULT NULL COMMENT '首播/上映日期',
    `imdb_rating`          decimal(3, 1) DEFAULT NULL COMMENT 'IMDb评分',
    `imdb_rating_count`    bigint        DEFAULT NULL COMMENT 'IMDb评分人数',
    `imdb_id`              varchar(30)   DEFAULT NULL COMMENT 'IMDb',
    `dou_ban_rating`       decimal(3, 1) DEFAULT NULL COMMENT '豆瓣评分',
    `dou_ban_rating_count` bigint        DEFAULT NULL COMMENT '豆瓣评分人数',
    `season`               varchar(30)   DEFAULT NULL COMMENT '季度',
    `episodes_count`       int           DEFAULT NULL COMMENT '集数',
    `durations`            varchar(30)   DEFAULT NULL COMMENT '单集片长',
    `directors`            varchar(2048) DEFAULT NULL COMMENT '导演',
    `actors`               varchar(2048) DEFAULT NULL COMMENT '主演/演员',
    `dramatist`            varchar(2048) DEFAULT NULL COMMENT '编剧',
    `tags`                 varchar(128)  DEFAULT NULL COMMENT '标签',
    `intro`                text COMMENT '简介',
    `awards`               text COMMENT '获奖情况',
    `create_time`          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY                    `title_index` (`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='豆瓣信息';