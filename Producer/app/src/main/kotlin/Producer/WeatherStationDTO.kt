package Producer

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.kittinunf.fuel.core.ResponseDeserializable

data class WeatherStationDTO(val id: String, val name: String, val location: String)

object WeatherStationDTODeserializer : ResponseDeserializable<List<WeatherStationDTO>> {
    override fun deserialize(content: String): List<WeatherStationDTO> {
        val results = mutableListOf<WeatherStationDTO>()
        val mapper = ObjectMapper().registerKotlinModule()
        val values: MappingIterator<WeatherStationDTO> = mapper.readerFor(WeatherStationDTO::class.java)
            .readValues(content)
        values.use {
            while (it.hasNextValue()) {
                results.add(it.nextValue())
            }
        }
        return results
    }
}
