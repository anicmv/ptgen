package com.github.anicmv.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author anicmv
 * @date 2025/3/28 21:49
 * @description TODO
 */
@Data
@Builder
public class IMDbData {
    //{
    //	"@meta": {
    //		"operation": "TitleRatings",
    //		"requestId": "9fc69166-c387-406d-af90-29744f890069",
    //		"serviceTimeMs": 11.056454
    //	},
    //	"resource": {
    //		"@type": "imdb.api.title.ratings",
    //		"canRate": true,
    //		"episode": 1,
    //		"id": "/title/tt31960864/",
    //		"rating": 9.1,
    //		"ratingCount": 10400,
    //		"season": 2,
    //		"title": "You Aren't E-Rank, Are You?",
    //		"titleType": "tvEpisode",
    //		"year": 2025
    //	}
    //}

    private String id;
    private String title;
    private String titleType;
    private Integer episode;
    private BigDecimal rating;
    private Integer ratingCount;
    private Integer season;
    private Integer year;

    private String buildImdbInfo() {
        if (rating != null && ratingCount != null) {
            return rating + "/10 from " + ratingCount + " users";
        }
        return null;
    }
}
