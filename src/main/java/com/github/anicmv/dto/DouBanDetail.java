package com.github.anicmv.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author anicmv
 * @date 2025/3/23 17:28
 * @description api dto对象
 */
@Data
@Builder
public class DouBanDetail {
    // 对应 JSON 的 "rating"
    private Rating rating;
    // 对应 JSON 的 "lineticket_url"
    private String lineticketUrl;
    // 对应 JSON 的 "ticket_price_info"
    private String ticketPriceInfo;
    // 对应 "controversy_reason"
    private String controversyReason;
    private List<String> pubdate;
    private List<String> shareActivities;
    // 对应 "interest_control_info"
    private Object interestControlInfo;
    // 对应 "last_episode_number"
    private Object lastEpisodeNumber;
    // 对应 JSON 的 "pic"
    private Pic pic;
    private String year;
    private int vendorCount;
    // 对应 "body_bg_color"
    private String bodyBgColor;
    // 对应 "is_tv"
    private boolean isTv;
    private String cardSubtitle;
    // 对应 "album_no_interact"
    private boolean albumNoInteract;
    private boolean canRate;
    private boolean webisode;
    private boolean isReleased;
    private String sharingUrl;


    private Object video;
    private String intro;
    private String headerBgColor;        // 对应 "header_bg_color"
    private String restrictiveIconUrl;        // 对应 "restrictive_icon_url"
    private String uri;
    private String id;
    private String coverUrl;             // 对应 "cover_url"
    private Cover cover;
    private String episodesInfo;         // 对应 "episodes_info"
    private boolean isRestrictive;       // 对应 "is_restrictive"
    private String vendorDesc;           // 对应 "vendor_desc"

    private int webisodeCount;

    private Object releaseDate;          // 对应 "release_date"
    private List<SimpleName> directors;
    private String title;

    private Object forumInfo;            // 对应 "forum_info"

    private List<String> countries;
    private List<Object> realtimeHotHonorInfos;
    private List<String> aka;
    private String url;
    private String rateInfo;             // 对应 "rate_info"
    private List<String> vendorIcons;    // 对应 "vendor_icons"
    private String nullRatingReason;     // 对应 "null_rating_reason"
    private String type;
    private String interestCmtEarlierTipDesc;
    private int reviewCount;             // 对应 "review_count"

    private boolean hasLinewatch;        // 对应 "has_linewatch"
    private int episodesCount;           // 对应 "episodes_count"
    private List<HonorInfo> honorInfos;  // 对应 "honor_infos"

    private Object prePlayableDate;      // 对应 "pre_playable_date"
    private int commentCount;            // 对应 "comment_count"
    private int forumTopicCount;            // 对应 "forum_topic_count"
    private List<Vendor> vendors;
    private String ticketPromoText;      // 对应 "ticket_promo_text"
    private List<String> languages;
    private List<String> durations;

    private List<SimpleName> actors;
    private String preReleaseDesc;       // 对应 "pre_release_desc"
    private String subtype;
    private String interestCmtEarlierTipTitle; // 对应 "interest_cmt_earlier_tip_title"
    private String infoUrl;              // 对应 "info_url"
    private Object headInfo;             // 对应 "head_info"
    private String wechatTimelineShare;  // 对应 "wechat_timeline_share"
    private List<String> genres;
    private String originalTitle;        // 对应 "original_title"
    private List<VariableModule> variableModules; // 对应 "variable_modules"
    private int galleryTopicCount;       // 对应 "gallery_topic_count"
    private ColorScheme colorScheme;
    private Object interest;             // 对应 "interest"
    private List<Trailer> trailers;
    private boolean isShow;              // 对应 "is_show"
    private List<String> ticketVendorIcons; // 对应 "ticket_vendor_icons"

    private boolean isDoubanIntro;       // 对应 "is_douban_intro"

    private List<SubjectCollection> subjectCollections;


    @Data
    @Builder
    public static class SubjectCollection {
        private boolean isFollow;
        private String title;
        private String id;
        private String uri;
    }


    @Data
    @Builder
    public static class Pic {
        private Author author;
        private String large;             // 对应 "large"
        private String normal;// 对应 "normal"
    }

    @Data
    @Builder
    public static class Cover {
        private Author author;
        private int position;
        private String ownerUri;             // 对应 "owner_uri"
        private String id;
        private String sharingUrl;           // 对应 "sharing_url"
        private CoverImage image;
        private String createTime;           // 对应 "create_time"
        private String description;
        private String type;
        private String uri;
        private String url;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String regTime;              // 对应 "reg_time"
        boolean isClub;              // 对应 "is_club"
        private String uri;
        private String avatarSideIcon;       // 对应 "avatar_side_icon"
        private String uid;
        private String url;
        private String type;
        private String avatar;
        int avatarSideIconType;      // 对应 "avatar_side_icon_type"
        private String avatarSideIconId;     // 对应 "avatar_side_icon_id"
        private Loc loc;
        private String kind;
        private String name;
    }

    @Data
    @Builder
    public static class Loc {
        private String id;
        private String name;
        private String uid;
    }

    @Data
    @Builder
    public static class CoverImage {
        private ImageInfo large;
        private Object raw;
        private String primaryColor;         // 对应 "primary_color"
        private ImageInfo normal;
        boolean isAnimated;          // 对应 "is_animated"
        private ImageInfo small;
    }

    @Data
    @Builder
    public static class ImageInfo {
        private int size;
        private String url;
        private int width;
        private int height;
    }

    @Data
    @Builder
    public static class Rating {
        private double value;
        private int starCount;               // 对应 "star_count"
        private int count;
        private int max;
    }

    @Data
    @Builder
    public static class HonorInfo {
        private String kind;
        private String title;
        private int rank;
        private String uri;
    }

    @Data
    @Builder
    public static class Vendor {
        private List<String> labels;
        private String style;
        private String title;
        private String promoteDesc;          // 对应 "promote_desc"
        private List<String> impressionTrackings; // 对应 "impression_trackings"
        private String url;
        private String appBundleId;          // 对应 "app_bundle_id"
        boolean isAd;                // 对应 "is_ad"
        private String icon;
        boolean isInWhitelist;       // 对应 "is_in_whitelist"
        private List<String> clickTrackings; // 对应 "click_trackings"
        private List<Payment> payments;
        private String impressionTracking;   // 对应 "impression_tracking"
        private String uri;
        private String id;
        private boolean isPaid;              // 对应 "is_paid"
        private String preReleaseDesc;       // 对应 "pre_release_desc"
        private String subjectId;            // 对应 "subject_id"
        private String paymentDesc;          // 对应 "payment_desc"
        private String appUri;               // 对应 "app_uri"
        private String bookTypeCn;           // 对应 "book_type_cn"
        private boolean accessible;
        private String greyIcon;             // 对应 "grey_icon"
        private String clickTracking;        // 对应 "click_tracking"
        private String bgImage;              // 对应 "bg_image"
        private String bookType;             // 对应 "book_type"
        private String episodesInfo;         // 对应 "episodes_info"
    }

    @Data
    @Builder
    public static class Payment {
        private String price;
        private String method;
        private String description;
    }

    @Data
    @Builder
    public static class ColorScheme {
        private List<Double> avgColor;      // 对应 "avg_color"
        private String primaryColorDark;     // 对应 "primary_color_dark"
        private String primaryColorLight;    // 对应 "primary_color_light"
        private List<Double> baseColor;     // 对应 "base_color"
        private String secondaryColor;       // 对应 "secondary_color"
        boolean isDark;
    }

    @Data
    @Builder
    public static class Trailer {
        private String id;
        private int termNum;                 // 对应 "term_num"
        private String uri;
        private String coverUrl;             // 对应 "cover_url"
        private String typeName;             // 对应 "type_name"
        private String runtime;
        private String title;
        private int nComments;               // 对应 "n_comments"
        private String type;
        private String videoUrl;             // 对应 "video_url"
        private String desc;
        private int fileSize;                // 对应 "file_size"
        private String createTime;           // 对应 "create_time"
        private String sharingUrl;           // 对应 "sharing_url"
    }

    @Data
    @Builder
    public static class VariableModule {
        private String id;
        private List<SubModule> subModules;  // 对应 "sub_modules"
    }

    @Data
    @Builder
    public static class SubModule {
        private String id;
        private Object data;               // 数据结构不固定，用 private Object 或 Map<private String, private Object> 接收
        private String dataType;           // 对应 "data_type"
    }

    @Data
    @Builder
    public static class SimpleName {
        private String name;
    }
}
