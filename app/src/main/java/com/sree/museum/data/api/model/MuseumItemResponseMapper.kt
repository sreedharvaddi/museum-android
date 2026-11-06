package com.sree.museum.data.api.model

import com.sree.museum.domain.model.MuseumItem

internal fun MuseumItemResponse.toDomain() : MuseumItem {
    return MuseumItem(
        id = objectID,
        primaryImageUrl = primaryImage,
        thumbnailUrl = primaryImageSmall,
        dated = objectDate,
        location = department,
        description = title
    )
}