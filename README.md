# PT Gen

一款Java语言编写的PT Gen信息生成程序。

## 运行环境

- JDK 21
- MySQL 8.0

## 技术选型

- Spring Boot 3.4.4
- Spring Security
- MyBatis Plus
- MySQL

## 特点

- 基于 Key 的分布式细粒度锁： 采用 ConcurrentHashMap 和 ReentrantLock 实现，该机制确保对于同一豆瓣 ID 的抓取操作仅在首次请求时执行，后续
  30 天内均可复用数据；到期后再更新存量数据。
- 降低豆瓣风控风险： 利用 MySQL 中存量数据一键生成 PT Gen 信息，有效减少直接抓取豆瓣页面的频率，从而降低风控触发的可能性。
- Token 认证： 利用 Spring Security 精细管理接口访问权限，确保安全可靠的接口访问控制。

## 开始使用

### PT Gen信息生成接口

接口地址: `GET /ptgen?url={豆瓣url或者id}`   
接口描述: 返回PT Gen字符串  
**请求头 (Headers)**

| 参数            | 必须 | 示例                  | 说明       |
|---------------|----|---------------------|----------|
| Authorization | 是  | Bearer <Your_Token> | Token 授权 |

**示例请求**

```
GET /ptgen?url={豆瓣url或者id}
```

**成功响应**

```
[img]https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2917604260.jpg[/img]

◎译　　名　我独自升级 第二季 -起于暗影- / 我独自升级：暗影崛起 / 我独自升级 第二季 -Arise from the Shadow- / 나 혼자만 레벨업 Season 2 / Solo Leveling Season 2 / Solo Leveling: Arise From the Shadow
◎片　　名　俺だけレベルアップな件 Season 2 -Arise from the Shadow-
◎年　　代　2025
◎产　　地　日本 / 韩国
◎类　　型　剧情 / 动作 / 动画 / 奇幻 / 冒险
◎官方网站　https://sololeveling-anime.net
◎语　　言　日语
◎上映日期　2025-01-04(日本)
◎豆瓣评分　8.8 from 7902 users
◎豆瓣链接　https://movie.douban.com/subject/36837352/
◎集　　数　13
◎片　　长　24分钟
◎导　　演　中重俊祐 Nakashige Shunsuke
◎编　　剧　秋空 Chugong
◎主　　演　坂泰斗 Ban Taito
　　　上田丽奈 Reina Ueda
　　　平川大辅 Daisuke Hirakawa
　　　东地宏树 Hiroki Touchi
　　　银河万丈 Banjô Ginga
　　　古川慎 Makoto Furukawa
　　　中村源太 Nakamura Genta
　　　三川华月 Mikawa Haruna

◎简　　介

　　自世界各地出现连接异次元与现实世界的通路「传送门」，已过了十多年。觉醒了特殊能力，被称为「猎人」的人们，与存在于传送门里地下城内的魔兽不断厮杀。猎人的能力在觉醒后就不再有成长空间，其等级也不会再有变化。然而，被称作是人类最弱兵器的程肖宇，在一次双重地下城的突击任务中得到了只有自己能够「升级」的能力，得以在战斗中不断变强。顺利通过转职任务，得到能操纵暗影士兵的职业「暗影君主」的肖宇，为了取得或许能医治母亲病情的道具「生命神水」的素材，而投身于新的战斗之中。

```

### 豆瓣详情信息接口

接口地址: `GET /detail?url={豆瓣url或者id}`   
接口描述: 返回PT Gen字符串  
**请求头 (Headers)**

| 参数            | 必须 | 示例                  | 说明       |
|---------------|----|---------------------|----------|
| Authorization | 是  | Bearer <Your_Token> | Token 授权 |

**示例请求**

```
GET /detail?url={豆瓣url或者id}
```

**成功响应**

```
{
    "douBan": {
        "id": 36837352,
        "title": "我独自升级 第二季 -起于暗影-",
        "type": "TVSeries",
        "originalTitle": "俺だけレベルアップな件 Season 2 -Arise from the Shadow-",
        "translatedName": "我独自升级 第二季 -起于暗影- / 我独自升级：暗影崛起 / 我独自升级 第二季 -Arise from the Shadow- / 나 혼자만 레벨업 Season 2 / Solo Leveling Season 2 / Solo Leveling: Arise From the Shadow",
        "year": 2025,
        "countries": "日本 / 韩国",
        "officialWebsite": "https://sololeveling-anime.net",
        "mainPic": "https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2917604260.jpg",
        "genres": "剧情 / 动作 / 动画 / 奇幻 / 冒险",
        "languages": "日语",
        "publishDate": "2025-01-04(日本)",
        "douBanScore": 8.8,
        "douBanPeople": "7916",
        "imdbId": "tt31960864",
        "episodesCount": 13,
        "durations": "PT0H24M",
        "directors": "中重俊祐 Nakashige Shunsuke",
        "actors": "坂泰斗 Ban Taito\n　　　上田丽奈 Reina Ueda\n　　　平川大辅 Daisuke Hirakawa\n　　　东地宏树 Hiroki Touchi\n　　　银河万丈 Banjô Ginga\n　　　古川慎 Makoto Furukawa\n　　　中村源太 Nakamura Genta\n　　　三川华月 Mikawa Haruna",
        "dramatist": "秋空 Chugong",
        "intro": "自世界各地出现连接异次元与现实世界的通路「传送门」，已过了十多年。觉醒了特殊能力，被称为「猎人」的人们，与存在于传送门里地下城内的魔兽不断厮杀。猎人的能力在觉醒后就不再有成长空间，其等级也不会再有变化。然而，被称作是人类最弱兵器的程肖宇，在一次双重地下城的突击任务中得到了只有自己能够「升级」的能力，得以在战斗中不断变强。顺利通过转职任务，得到能操纵暗影士兵的职业「暗影君主」的肖宇，为了取得或许能医治母亲病情的道具「生命神水」的素材，而投身于新的战斗之中。",
        "awards": "{}",
        "createTime": "2025-03-24T14:48:34.77171352",
        "updateTime": "2025-03-24T14:48:34.771813396"
    },
    "douBanPage": {
        "context": "http://schema.org",
        "name": "我独自升级 第二季 -起于暗影- 俺だけレベルアップな件 Season 2 -Arise from the Shadow-",
        "imdb": "tt31960864",
        "genres": "剧情 / 动作 / 动画 / 奇幻 / 冒险",
        "url": "/subject/36837352/",
        "image": "https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2917604260.jpg",
        "languages": "日语",
        "episodesCount": 13,
        "officialWebsite": "https://sololeveling-anime.net",
        "firstBroadcast": "2025-01-04(日本)",
        "countries": "日本 / 韩国",
        "director": [
            {
                "type": "Person",
                "url": "/celebrity/1509565/",
                "name": "中重俊祐 Nakashige Shunsuke"
            }
        ],
        "author": [
            {
                "type": "Person",
                "url": "/celebrity/1523080/",
                "name": "秋空 Chugong"
            }
        ],
        "actor": [
            {
                "type": "Person",
                "url": "/celebrity/1397449/",
                "name": "坂泰斗 Ban Taito"
            },
            {
                "type": "Person",
                "url": "/celebrity/1351541/",
                "name": "上田丽奈 Reina Ueda"
            },
            {
                "type": "Person",
                "url": "/celebrity/1020547/",
                "name": "平川大辅 Daisuke Hirakawa"
            },
            {
                "type": "Person",
                "url": "/celebrity/1177363/",
                "name": "东地宏树 Hiroki Touchi"
            },
            {
                "type": "Person",
                "url": "/celebrity/1042350/",
                "name": "银河万丈 Banjô Ginga"
            },
            {
                "type": "Person",
                "url": "/celebrity/1348459/",
                "name": "古川慎 Makoto Furukawa"
            },
            {
                "type": "Person",
                "url": "/celebrity/1446052/",
                "name": "中村源太 Nakamura Genta"
            },
            {
                "type": "Person",
                "url": "/celebrity/1474240/",
                "name": "三川华月 Mikawa Haruna"
            }
        ],
        "datePublished": "2025-01-04",
        "genre": [
            "剧情",
            "动作",
            "动画",
            "奇幻",
            "冒险"
        ],
        "duration": "PT0H24M",
        "description": "自世界各地出现连接异次元与现实世界的通路「传送门」，已过了十多年。觉醒了特殊能力，被称为「猎人」的人们，与存在于传送门里地下城内的魔兽不断厮杀。猎人的能力在觉醒后就不再有成长空间，其等级也不会再有变化...",
        "type": "TVSeries",
        "aggregateRating": {
            "type": "AggregateRating",
            "ratingCount": 7832,
            "bestRating": 10,
            "worstRating": 2,
            "ratingValue": 8.8
        }
    },
    "douBanDetail": {
        "rating": {
            "value": 8.8,
            "starCount": 4,
            "count": 7916,
            "max": 10
        },
        "lineticketUrl": "",
        "ticketPriceInfo": "",
        "controversyReason": "",
        "pubdate": [
            "2025-01-04(日本)"
        ],
        "shareActivities": [],
        "pic": {
            "large": "https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2917604260.jpg",
            "normal": "https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2917604260.jpg"
        },
        "year": "2025",
        "vendorCount": 1,
        "bodyBgColor": "f4f6f9",
        "isTv": true,
        "cardSubtitle": "2025 / 日本 韩国 / 剧情 动作 动画 奇幻 冒险 / 中重俊祐 / 坂泰斗 上田丽奈",
        "albumNoInteract": false,
        "canRate": true,
        "webisode": false,
        "isReleased": true,
        "sharingUrl": "https://www.douban.com/doubanapp/dispatch/movie/36837352",
        "intro": "自世界各地出现连接异次元与现实世界的通路「传送门」，已过了十多年。觉醒了特殊能力，被称为「猎人」的人们，与存在于传送门里地下城内的魔兽不断厮杀。猎人的能力在觉醒后就不再有成长空间，其等级也不会再有变化。然而，被称作是人类最弱兵器的程肖宇，在一次双重地下城的突击任务中得到了只有自己能够「升级」的能力，得以在战斗中不断变强。顺利通过转职任务，得到能操纵暗影士兵的职业「暗影君主」的肖宇，为了取得或许能医治母亲病情的道具「生命神水」的素材，而投身于新的战斗之中。",
        "headerBgColor": "252f4c",
        "restrictiveIconUrl": "",
        "uri": "douban://douban.com/tv/36837352",
        "id": "36837352",
        "coverUrl": "https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2917604260.jpg",
        "cover": {
            "author": {
                "id": "81796416",
                "regTime": "2014-01-01 15:47:10",
                "isClub": false,
                "uri": "douban://douban.com/user/81796416",
                "uid": "dinokiller912",
                "url": "https://www.douban.com/people/81796416/",
                "type": "user",
                "avatar": "https://img9.doubanio.com/icon/up81796416-6.jpg",
                "avatarSideIconType": 0,
                "loc": {
                    "id": "108296",
                    "name": "上海",
                    "uid": "shanghai"
                },
                "kind": "user",
                "name": "屠龙侠912"
            },
            "position": 1,
            "ownerUri": "douban://douban.com/tv/36837352",
            "id": "2917604260",
            "sharingUrl": "https://www.douban.com/doubanapp/dispatch?uri=/photo/2917604260/",
            "image": {
                "large": {
                    "size": 0,
                    "url": "https://img1.doubanio.com/view/photo/l/public/p2917604260.jpg",
                    "width": 903,
                    "height": 1280
                },
                "primaryColor": "DFDFDF",
                "normal": {
                    "size": 0,
                    "url": "https://img1.doubanio.com/view/photo/m/public/p2917604260.jpg",
                    "width": 423,
                    "height": 600
                },
                "isAnimated": false,
                "small": {
                    "size": 0,
                    "url": "https://img1.doubanio.com/view/photo/s/public/p2917604260.jpg",
                    "width": 423,
                    "height": 600
                }
            },
            "createTime": "2025-01-20 19:56:46",
            "description": "",
            "type": "photo",
            "uri": "douban://douban.com/photo/2917604260",
            "url": "https://movie.douban.com/photos/photo/2917604260/"
        },
        "episodesInfo": "更新至9集",
        "isRestrictive": false,
        "vendorDesc": "",
        "webisodeCount": 0,
        "directors": [
            {
                "name": "中重俊祐"
            }
        ],
        "title": "我独自升级 第二季 -起于暗影-",
        "countries": [
            "日本",
            "韩国"
        ],
        "realtimeHotHonorInfos": [],
        "aka": [
            "我独自升级：暗影崛起",
            "我独自升级 第二季 -Arise from the Shadow-",
            "나 혼자만 레벨업 Season 2",
            "Solo Leveling Season 2",
            "Solo Leveling: Arise From the Shadow"
        ],
        "url": "https://movie.douban.com/subject/36837352/",
        "rateInfo": "",
        "vendorIcons": [
            "https://img9.doubanio.com/f/frodo/88a62f5e0cf9981c910e60f4421c3e66aac2c9bc/pics/vendors/bilibili.png"
        ],
        "nullRatingReason": "",
        "type": "tv",
        "interestCmtEarlierTipDesc": "该短评的发布时间早于公开上映时间，作者可能通过其他渠道提前观看，请谨慎参考。其评分将不计入总评分。",
        "reviewCount": 5,
        "hasLinewatch": true,
        "episodesCount": 13,
        "honorInfos": [],
        "commentCount": 1674,
        "forumTopicCount": 1,
        "vendors": [
            {
                "labels": [],
                "style": "large",
                "title": "哔哩哔哩",
                "promoteDesc": "",
                "impressionTrackings": [
                    "https://frodo.douban.com/rohirrim/video_tracking/impression?subject_id=36837352&video_type=movie&video_id=996999&source=bilibili&user_id=None&douban_udid=None&platform=&location=vendor_section"
                ],
                "url": "https://m.bilibili.com/bangumi/play/ep1403521?bsource=doubanh5",
                "appBundleId": "tv.danmaku.bili",
                "isAd": false,
                "icon": "https://img9.doubanio.com/f/frodo/88a62f5e0cf9981c910e60f4421c3e66aac2c9bc/pics/vendors/bilibili.png",
                "isInWhitelist": false,
                "clickTrackings": [
                    "https://frodo.douban.com/rohirrim/video_tracking/click?subject_id=36837352&video_type=movie&video_id=996999&source=bilibili&user_id=None&douban_udid=None&platform=&location=vendor_section"
                ],
                "payments": [
                    {
                        "price": "",
                        "method": "VIP免费观看",
                        "description": ""
                    }
                ],
                "impressionTracking": "",
                "uri": "bilibili://pgc/season/80763?h5awaken=b3Blbl9hcHBfZnJvbV90eXBlPWg1Jm9wZW5fYXBwX2FkZGl0aW9uPSU3QiUyMmJzb3VyY2UlMjIlM0ElMjJkb3ViYW5kcSUyMiUyQyUyMml2a19mcm9tJTIyJTNBJTIyZG91YmFuZHElMjIlN0Q=",
                "id": "bilibili",
                "isPaid": true,
                "preReleaseDesc": "",
                "subjectId": "36837352",
                "paymentDesc": "VIP免费观看",
                "appUri": "bilibili://skynet.douban.com",
                "bookTypeCn": "",
                "accessible": true,
                "greyIcon": "https://img2.doubanio.com/f/frodo/306bfa7ea3d607e3525063ac6aea156b2ca163f5/pics/vendors/bilibili_grey.png",
                "clickTracking": "",
                "bgImage": "",
                "bookType": "",
                "episodesInfo": "更新至9集"
            }
        ],
        "ticketPromoText": "",
        "languages": [
            "日语"
        ],
        "durations": [
            "24分钟"
        ],
        "actors": [
            {
                "name": "坂泰斗"
            },
            {
                "name": "上田丽奈"
            },
            {
                "name": "平川大辅"
            },
            {
                "name": "东地宏树"
            },
            {
                "name": "银河万丈"
            },
            {
                "name": "古川慎"
            },
            {
                "name": "中村源太"
            },
            {
                "name": "三川华月"
            }
        ],
        "preReleaseDesc": "",
        "subtype": "tv",
        "interestCmtEarlierTipTitle": "发布于上映前",
        "infoUrl": "https://www.douban.com/doubanapp//h5/movie/36837352/desc",
        "wechatTimelineShare": "screenshot",
        "genres": [
            "剧情",
            "动作",
            "动画"
        ],
        "originalTitle": "俺だけレベルアップな件 Season 2 -Arise from the Shadow-",
        "variableModules": [
            {
                "id": "rating",
                "subModules": []
            },
            {
                "id": "other_interests",
                "subModules": []
            },
            {
                "id": "video_photos",
                "subModules": []
            },
            {
                "id": "episode_discussions",
                "subModules": []
            },
            {
                "id": "tags",
                "subModules": []
            },
            {
                "id": "share",
                "subModules": [
                    {
                        "id": "screenshot"
                    },
                    {
                        "id": "doulist"
                    },
                    {
                        "id": "status"
                    },
                    {
                        "id": "other_channels"
                    }
                ]
            },
            {
                "id": "honor_infos",
                "subModules": []
            },
            {
                "id": "comments",
                "subModules": [
                    {
                        "id": "count"
                    },
                    {
                        "id": "sort_by",
                        "data": [
                            {
                                "name": "热门",
                                "id": "hot"
                            },
                            {
                                "name": "最新",
                                "id": "latest"
                            },
                            {
                                "name": "友邻",
                                "id": "friend"
                            }
                        ],
                        "dataType": "sort_by"
                    },
                    {
                        "id": "rating_scope"
                    }
                ]
            },
            {
                "id": "related_items",
                "subModules": []
            },
            {
                "id": "interest",
                "subModules": []
            },
            {
                "id": "related_subjects",
                "subModules": []
            },
            {
                "id": "ugc_tabs",
                "subModules": [
                    {
                        "id": "ugc_tab",
                        "data": {
                            "source": "reviews",
                            "title": "剧评",
                            "type": "review",
                            "sort_by": [
                                {
                                    "name": "热门",
                                    "id": "hot"
                                },
                                {
                                    "name": "最新",
                                    "id": "latest"
                                },
                                {
                                    "name": "友邻",
                                    "id": "friend"
                                }
                            ]
                        },
                        "dataType": "ugc_tab"
                    },
                    {
                        "id": "ugc_tab",
                        "data": {
                            "count": 98,
                            "title": "小组讨论",
                            "uri": "douban://partial.douban.com/subject/36837352/group_topic/_content?group_id=740749&topic_tag_id=157923",
                            "source": "小组讨论",
                            "type": "custom",
                            "id": "group"
                        },
                        "dataType": "ugc_tab"
                    }
                ]
            }
        ],
        "galleryTopicCount": 0,
        "colorScheme": {
            "primaryColorDark": "252f4c",
            "primaryColorLight": "384772",
            "secondaryColor": "f4f6f9",
            "isDark": true
        },
        "trailers": [
            {
                "id": "319112",
                "termNum": 0,
                "uri": "douban://douban.com/tv/36837352/trailer?trailer_id=319112&trailer_type=A",
                "coverUrl": "https://img1.doubanio.com/img/trailer/medium/2916770880.jpg",
                "typeName": "预告片",
                "runtime": "02:22",
                "title": "预告片",
                "nComments": 1,
                "type": "A",
                "videoUrl": "https://vt1.doubanio.com/202503242234/ea8e6c37f2776963424a7595f4ba1032/view/movie/M/703190112.mp4",
                "desc": "",
                "fileSize": 51930883,
                "createTime": "2024-12-26",
                "sharingUrl": "https://www.douban.com/doubanapp/dispatch?uri=/tv/36837352/trailer%3Ftrailer_id%3D319112%26trailer_type%3DA"
            }
        ],
        "isShow": false,
        "ticketVendorIcons": [
            "https://img9.doubanio.com/view/dale-online/dale_ad/public/0589a62f2f2d7c2.jpg"
        ],
        "isDoubanIntro": false,
        "subjectCollections": []
    },
    "ptGen": "[img]https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2917604260.jpg[/img]\n\n◎译　　名　我独自升级 第二季 -起于暗影- / 我独自升级：暗影崛起 / 我独自升级 第二季 -Arise from the Shadow- / 나 혼자만 레벨업 Season 2 / Solo Leveling Season 2 / Solo Leveling: Arise From the Shadow\n◎片　　名　俺だけレベルアップな件 Season 2 -Arise from the Shadow-\n◎年　　代　2025\n◎产　　地　日本 / 韩国\n◎类　　型　剧情 / 动作 / 动画 / 奇幻 / 冒险\n◎官方网站　https://sololeveling-anime.net\n◎语　　言　日语\n◎上映日期　2025-01-04(日本)\n◎豆瓣评分　8.8 from 7916 users\n◎豆瓣链接　https://movie.douban.com/subject/36837352/\n◎集　　数　13\n◎片　　长　24分钟\n◎导　　演　中重俊祐 Nakashige Shunsuke\n◎编　　剧　秋空 Chugong\n◎主　　演　坂泰斗 Ban Taito\n　　　上田丽奈 Reina Ueda\n　　　平川大辅 Daisuke Hirakawa\n　　　东地宏树 Hiroki Touchi\n　　　银河万丈 Banjô Ginga\n　　　古川慎 Makoto Furukawa\n　　　中村源太 Nakamura Genta\n　　　三川华月 Mikawa Haruna\n\n◎简　　介\n\n　　自世界各地出现连接异次元与现实世界的通路「传送门」，已过了十多年。觉醒了特殊能力，被称为「猎人」的人们，与存在于传送门里地下城内的魔兽不断厮杀。猎人的能力在觉醒后就不再有成长空间，其等级也不会再有变化。然而，被称作是人类最弱兵器的程肖宇，在一次双重地下城的突击任务中得到了只有自己能够「升级」的能力，得以在战斗中不断变强。顺利通过转职任务，得到能操纵暗影士兵的职业「暗影君主」的肖宇，为了取得或许能医治母亲病情的道具「生命神水」的素材，而投身于新的战斗之中。\n"
}
```

### 直接从存量数据库生成PT Gen信息

接口地址: `GET /old?url={豆瓣url或者id}`   
接口描述: 返回PT Gen字符串  
**请求头 (Headers)**

| 参数            | 必须 | 示例                  | 说明       |
|---------------|----|---------------------|----------|
| Authorization | 是  | Bearer <Your_Token> | Token 授权 |

**示例请求**

```
GET /old?url={豆瓣url或者id}
```

**成功响应**

```
[img]https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2917604260.jpg[/img]

◎译　　名　我独自升级 第二季 -起于暗影- / 我独自升级：暗影崛起 / 我独自升级 第二季 -Arise from the Shadow- / 나 혼자만 레벨업 Season 2 / Solo Leveling Season 2 / Solo Leveling: Arise From the Shadow
◎片　　名　俺だけレベルアップな件 Season 2 -Arise from the Shadow-
◎年　　代　2025
◎产　　地　日本 / 韩国
◎类　　型　剧情 / 动作 / 动画 / 奇幻 / 冒险
◎官方网站　https://sololeveling-anime.net
◎语　　言　日语
◎上映日期　2025-01-04(日本)
◎豆瓣评分　8.8 from 7902 users
◎豆瓣链接　https://movie.douban.com/subject/36837352/
◎集　　数　13
◎片　　长　24分钟
◎导　　演　中重俊祐 Nakashige Shunsuke
◎编　　剧　秋空 Chugong
◎主　　演　坂泰斗 Ban Taito
　　　上田丽奈 Reina Ueda
　　　平川大辅 Daisuke Hirakawa
　　　东地宏树 Hiroki Touchi
　　　银河万丈 Banjô Ginga
　　　古川慎 Makoto Furukawa
　　　中村源太 Nakamura Genta
　　　三川华月 Mikawa Haruna

◎简　　介

　　自世界各地出现连接异次元与现实世界的通路「传送门」，已过了十多年。觉醒了特殊能力，被称为「猎人」的人们，与存在于传送门里地下城内的魔兽不断厮杀。猎人的能力在觉醒后就不再有成长空间，其等级也不会再有变化。然而，被称作是人类最弱兵器的程肖宇，在一次双重地下城的突击任务中得到了只有自己能够「升级」的能力，得以在战斗中不断变强。顺利通过转职任务，得到能操纵暗影士兵的职业「暗影君主」的肖宇，为了取得或许能医治母亲病情的道具「生命神水」的素材，而投身于新的战斗之中。

```

### PT Gen数据保存

接口地址: `POST /save   
接口描述: 返回PT Gen字符串  
**请求头 (Headers)**

| 参数            | 必须 | 示例                  | 说明       |
|---------------|----|---------------------|----------|
| Authorization | 是  | Bearer <Your_Token> | Token 授权 |

**示例请求**

```
POST /save
```

**请求头 (Headers)**

| 参数            | 必须 | 示例                  | 说明                 |
|---------------|----|---------------------|--------------------|
| Content-Type  | 是  | multipart/form-data | 指定表单格式，包含文件在内的表单字段 |
| Authorization | 是  | Bearer <Your_Token> | Token 授权           |

**请求参数 (Body - raw json)**

```
{
    "id": 36837352,
    "title": "我独自升级 第二季 -起于暗影-",
    "type": "TVSeries",
    "originalTitle": "俺だけレベルアップな件 Season 2 -Arise from the Shadow-",
    "translatedName": "我独自升级 第二季 -起于暗影- / 我独自升级：暗影崛起 / 我独自升级 第二季 -Arise from the Shadow- / 나 혼자만 레벨업 Season 2 / Solo Leveling Season 2 / Solo Leveling: Arise From the Shadow",
    "year": 2025,
    "countries": "日本 / 韩国",
    "officialWebsite": "https://sololeveling-anime.net",
    "mainPic": "https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2917604260.jpg",
    "genres": "剧情 / 动作 / 动画 / 奇幻 / 冒险",
    "languages": "日语",
    "publishDate": "2025-01-04(日本)",
    "douBanScore": 8.8,
    "douBanPeople": "7916",
    "imdbId": "tt31960864",
    "episodesCount": 13,
    "durations": "PT0H24M",
    "directors": "中重俊祐 Nakashige Shunsuke",
    "actors": "坂泰斗 Ban Taito\n　　　上田丽奈 Reina Ueda\n　　　平川大辅 Daisuke Hirakawa\n　　　东地宏树 Hiroki Touchi\n　　　银河万丈 Banjô Ginga\n　　　古川慎 Makoto Furukawa\n　　　中村源太 Nakamura Genta\n　　　三川华月 Mikawa Haruna",
    "dramatist": "秋空 Chugong",
    "intro": "自世界各地出现连接异次元与现实世界的通路「传送门」，已过了十多年。觉醒了特殊能力，被称为「猎人」的人们，与存在于传送门里地下城内的魔兽不断厮杀。猎人的能力在觉醒后就不再有成长空间，其等级也不会再有变化。然而，被称作是人类最弱兵器的程肖宇，在一次双重地下城的突击任务中得到了只有自己能够「升级」的能力，得以在战斗中不断变强。顺利通过转职任务，得到能操纵暗影士兵的职业「暗影君主」的肖宇，为了取得或许能医治母亲病情的道具「生命神水」的素材，而投身于新的战斗之中。",
    "awards": "{1}",
    "createTime": "2025-03-24T14:48:34.77171352",
    "updateTime": "2025-03-24T14:48:34.771813396"
}
```

**成功响应**

```
{
    "result": "success",
    "code": 200,
    "data": "ok"
}
```

### 从存量数据中获取不过期的数据

接口地址: `GET /cache?url={豆瓣url或者id}`   
接口描述: 返回JSON  
**请求头 (Headers)**

| 参数            | 必须 | 示例                  | 说明       |
|---------------|----|---------------------|----------|
| Authorization | 是  | Bearer <Your_Token> | Token 授权 |

**示例请求**

```
GET /cache?url={豆瓣url或者id}
```

**成功响应**   
缓存未过期

```
{
    "result": "success",
    "code": 200,
    "data": "[img]https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2917604260.jpg[/img]\n\n◎译　　名　我独自升级 第二季 -起于暗影- / 我独自升级：暗影崛起 / 我独自升级 第二季 -Arise from the Shadow- / 나 혼자만 레벨업 Season 2 / Solo Leveling Season 2 / Solo Leveling: Arise From the Shadow\n◎片　　名　俺だけレベルアップな件 Season 2 -Arise from the Shadow-\n◎年　　代　2025\n◎产　　地　日本 / 韩国\n◎类　　型　剧情 / 动作 / 动画 / 奇幻 / 冒险\n◎官方网站　https://sololeveling-anime.net\n◎语　　言　日语\n◎上映日期　2025-01-04(日本)\n◎豆瓣评分　8.8 from 7916 users\n◎豆瓣链接　https://movie.douban.com/subject/36837352/\n◎集　　数　13\n◎片　　长　24分钟\n◎导　　演　中重俊祐 Nakashige Shunsuke\n◎编　　剧　秋空 Chugong\n◎主　　演　坂泰斗 Ban Taito\n　　　上田丽奈 Reina Ueda\n　　　平川大辅 Daisuke Hirakawa\n　　　东地宏树 Hiroki Touchi\n　　　银河万丈 Banjô Ginga\n　　　古川慎 Makoto Furukawa\n　　　中村源太 Nakamura Genta\n　　　三川华月 Mikawa Haruna\n\n◎简　　介\n\n　　自世界各地出现连接异次元与现实世界的通路「传送门」，已过了十多年。觉醒了特殊能力，被称为「猎人」的人们，与存在于传送门里地下城内的魔兽不断厮杀。猎人的能力在觉醒后就不再有成长空间，其等级也不会再有变化。然而，被称作是人类最弱兵器的程肖宇，在一次双重地下城的突击任务中得到了只有自己能够「升级」的能力，得以在战斗中不断变强。顺利通过转职任务，得到能操纵暗影士兵的职业「暗影君主」的肖宇，为了取得或许能医治母亲病情的道具「生命神水」的素材，而投身于新的战斗之中。\n"
}
```

缓存已过期

```
{
"result": "success",
"code": -1,
"data": "豆瓣: 36837352, expired"
}
```

无存量数据

```
{
"result": "success",
"code": -1,
"data": "豆瓣: 36837352, not exist"
}
```

## 不会搭建?

> 可以到[Telegram群组](https://t.me/+q5wtfnj0kTs4MTRl)来讨论各种问题
