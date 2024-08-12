package com.ymo.data.mapper

import com.ymo.data.model.api.Feed
import com.ymo.data.model.db.FeedEntity

fun Feed.toEntity() = FeedEntity(
    id = id ?: 0,
    imagefile = imageFile,
    avatar = avatar,
    createdDate = createdDate,
    topComments = topComments
)

fun FeedEntity.toModel() = Feed(
    id = id,
    imageFile = imagefile,
    avatar = avatar,
    createdDate = createdDate,
    topComments = topComments
)

fun List<Feed>?.toEntityList() = this?.map { it.toEntity() }

fun List<FeedEntity>?.toModelList() = this?.map { it.toModel() }