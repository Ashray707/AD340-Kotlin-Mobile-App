package AD340.HomeworkPractice


data class CameraModel(
    val Id: String, val Description: String,
    val ImageUrl: String, val Type: String) {

/*
data class CameraModel (
    val Id: String,
    val Description: String,
    val ImageUrl: String,
    val Type: String
)

// some sort of if else condition
// for type sdot or wsdot

    fun ImageUrl(): String {
    // Type == sdot?
        return if (Type.equals("sdot")) {
            "http://www.seattle.gov/trafficcams/images/"
        } else {
            "http://images.wsdot.wa.gov/nw/"
        }
    }
*/

    private val baseUrl: Map<String, String> = mapOf(
        "sdot" to "https://www.seattle.gov/trafficcams/images/",
        "wsdot" to "https://images.wsdot.wa.gov/nw/"
    )

    fun imageUrl(): String {
        return baseUrl[this.Type] + this.ImageUrl
    }
}