package de.gally.bonsai.port.rest.controller

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.gally.bonsai.config.CacheableConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
@RequestMapping("/api/v1/weather")
class WeatherController(
    @Value("\${bonsai.weather.api-url}") private val weatherApiUrl: String,
    @Value("\${bonsai.weather.api-key}") private val weatherApiKey: String,
    @Value("\${bonsai.weather.lat}") private val weatherLat: String,
    @Value("\${bonsai.weather.long}") private val weatherLong: String,
) {
    private val restClient: RestClient by lazy {
        RestClient.create(weatherApiUrl)
    }

    /**
     * As each request against the weather api is limited per day we don't want to spam the api.
     * So we cache the result and update the cache after a specific time.
     */
    @GetMapping
    @Cacheable(CacheableConfiguration.WEATHER_CACHE_NAME)
    fun getCurrentWeather(): WeatherResponse? {
        return restClient.get()
            .uri {
                it
                    .queryParam("lat", weatherLat)
                    .queryParam("lon", weatherLong)
                    .queryParam("appid", weatherApiKey)
                    .queryParam("units", "metric")
                    .queryParam("lang", "de")
                    .build()
            }.retrieve()
            .body(WeatherResponse::class.java)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherResponse(
        val name: String,
        val main: Main,
        val weather: List<Weather>
    ) {
        data class Main(
            val temp: String,
        )

        data class Weather(
            val main: String
        )
    }
}