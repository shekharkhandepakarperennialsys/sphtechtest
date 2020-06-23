@Parcelize
data class Response(

	@Json(name="result")
	val result: Result? = null,

	@Json(name="help")
	val help: String? = null,

	@Json(name="success")
	val success: Boolean? = null
) : Parcelable

@Parcelize
data class Links(

	@Json(name="next")
	val next: String? = null,

	@Json(name="start")
	val start: String? = null
) : Parcelable

@Parcelize
data class RecordsItem(

	@Json(name="volume_of_mobile_data")
	val volumeOfMobileData: String? = null,

	@Json(name="_id")
	val id: Int? = null,

	@Json(name="quarter")
	val quarter: String? = null
) : Parcelable

@Parcelize
data class FieldsItem(

	@Json(name="id")
	val id: String? = null,

	@Json(name="type")
	val type: String? = null
) : Parcelable

@Parcelize
data class Result(

	@Json(name="total")
	val total: Int? = null,

	@Json(name="records")
	val records: List<RecordsItem?>? = null,

	@Json(name="_links")
	val links: Links? = null,

	@Json(name="limit")
	val limit: Int? = null,

	@Json(name="resource_id")
	val resourceId: String? = null,

	@Json(name="fields")
	val fields: List<FieldsItem?>? = null
) : Parcelable

