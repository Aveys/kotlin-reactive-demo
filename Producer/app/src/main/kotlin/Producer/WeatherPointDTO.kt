package Producer

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WeatherPointDTO(
    val temp: Double,
    val unit: String,
    val wind: String
)
