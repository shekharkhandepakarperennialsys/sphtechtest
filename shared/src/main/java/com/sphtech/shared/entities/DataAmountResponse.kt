package com.sphtech.shared.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class DataAmountResponse(
    @SerializedName("help") val help: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("result") val result: DataAmountData
)

data class DataAmountData(
    @SerializedName("resource_id") val resourceId: String,
    @SerializedName("fields") val fields: List<FieldsData>,
    @SerializedName("records") val records: List<RecordsData>,
    @SerializedName("_links") val links: LinksData,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int
)

data class FieldsData(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: String
)

@Entity(tableName = "records_data")
data class RecordsData(
    @SerializedName("volume_of_mobile_data") val volumeOfMobileData: String,
    @SerializedName("quarter") val quarter: String,
    @PrimaryKey @SerializedName("_id") val id: Int
)

data class LinksData(
    @SerializedName("start") val start: String,
    @SerializedName("next") val next: String
)