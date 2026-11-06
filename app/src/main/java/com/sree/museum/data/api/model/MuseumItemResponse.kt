package com.sree.museum.data.api.model

data class MuseumItemResponse(
    val objectID: Int,
    val isHighlight: Boolean = false,
    val accessionNumber: String = "",
    val accessionYear: String = "",
    val isPublicDomain: Boolean = false,
    val primaryImage: String = "",
    val primaryImageSmall: String = "",
    val additionalImages: List<String> = emptyList(),
    val constituents: List<Constituent> = emptyList(),
    val department: String = "",
    val objectName: String = "",
    val title: String = "",
    val culture: String = "",
    val period: String = "",
    val dynasty: String = "",
    val reign: String = "",
    val portfolio: String = "",
    val artistRole: String = "",
    val artistPrefix: String = "",
    val artistDisplayName: String = "",
    val artistDisplayBio: String = "",
    val artistSuffix: String = "",
    val artistAlphaSort: String = "",
    val artistNationality: String = "",
    val artistBeginDate: String = "",
    val artistEndDate: String = "",
    val artistGender: String = "",
    val artistWikidata_URL: String = "",
    val artistULAN_URL: String = "",
    val objectDate: String = "",
    val objectBeginDate: Int? = null,
    val objectEndDate: Int? = null,
    val medium: String = "",
    val dimensions: String = "",
    val measurements: List<Measurement> = emptyList(),
    val creditLine: String = "",
    val geographyType: String = "",
    val city: String = "",
    val state: String = "",
    val county: String = "",
    val country: String = "",
    val region: String = "",
    val subregion: String = "",
    val locale: String = "",
    val locus: String = "",
    val excavation: String = "",
    val river: String = "",
    val classification: String = "",
    val rightsAndReproduction: String = "",
    val linkResource: String = "",
    val metadataDate: String = "",
    val repository: String = "",
    val objectURL: String = "",
    val tags: List<Tag> = emptyList(),
    val objectWikidata_URL: String = "",
    val isTimelineWork: Boolean = false,
    val galleryNumber: String = ""
)

data class Constituent(
    val constituentID: Int = 0,
    val role: String = "",
    val name: String = "",
    val constituentULAN_URL: String = "",
    val constituentWikidata_URL: String = "",
    val gender: String = ""
)

data class Tag(
    val term: String = "",
    val AAT_URL: String = "",
    val Wikidata_URL: String = ""
)

data class Measurement(
    val elementName: String = "",
    val elementDescription: String? = null,
    val elementMeasurements: Map<String, Double> = emptyMap()
)
