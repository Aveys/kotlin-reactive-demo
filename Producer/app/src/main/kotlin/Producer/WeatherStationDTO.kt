package Producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.kittinunf.fuel.core.ResponseDeserializable

data class WeatherStationDTO(val id: String, val name: String, val location: String)

object WeatherStationDTODeserializer : ResponseDeserializable<List<WeatherStationDTO>> {
    override fun deserialize(content: String) =
        ObjectMapper().registerKotlinModule().readValue<List<WeatherStationDTO>>(content)
}
