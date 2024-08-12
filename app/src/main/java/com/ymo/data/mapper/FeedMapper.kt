package com.ymo.data.mapper

import com.ymo.data.model.api.Feed
import com.ymo.data.model.db.FeedEntity

fun Feed.toEntity() = FeedEntity(
    id = id ?: 0,
    imagefile = imagefile,
    avatar = avatar,
    createddate = createddate,
    topComments = topcomments
)

fun FeedEntity.toModel() = Feed(
    id = id,
    imagefile = imagefile,
    avatar = avatar,
    createddate = createddate,
    topcomments = topComments
)

fun List<Feed>?.toEntityList() = this?.map { it.toEntity() }

fun List<FeedEntity>?.toModelList() = this?.map { it.toModel() }
