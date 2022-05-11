package AD340.HomeworkPractice


data class CameraModel(
    val Id: String, val Description: String,
    val ImageUrl: String, val Type: String) {

    private val baseUrl: Map<String, String> = mapOf(
        "sdot" to "https://www.seattle.gov/trafficcams/images/",
        "wsdot" to "https://images.wsdot.wa.gov/nw/"
    )

    fun imageUrl(): String {
        return baseUrl[this.Type] + this.ImageUrl
    }
}