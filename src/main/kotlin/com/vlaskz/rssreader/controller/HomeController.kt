package com.vlaskz.rssreader.controller

import com.vlaskz.rssreader.client.NtpClient
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
class HomeController {
    @CrossOrigin(origins = ["*"])
    @GetMapping("/date-time")
    fun streamDateTime(@RequestParam(required = false) timeZone: String): Flux<ServerSentEvent<String>> {
        val zoneId = try {
            ZoneId.of(timeZone ?: "UTC")
        } catch (e: Exception) {
            ZoneId.systemDefault()
        }

        return Flux.interval(Duration.ofSeconds(1))
            .map {
                ServerSentEvent.builder<String>()
                    .id(UUID.randomUUID().toString())
                    .data(NtpClient.getCurrentNtpTime(zoneId) toFormat ("HH:mm:ss dd/MM/yyyy"))
                    .build()
            }

    }

    infix fun LocalDateTime.toFormat(pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return this.format(formatter)
    }
}
/**
 *  curl 'https://server.growatt.com/panel/tlx/getTLXTotalData?plantId=2467181' \
 *   -H 'authority: server.growatt.com' \
 *   -H 'accept: application/json, text/javascript, 
*; q=0.01' \
* -H 'accept-language: en-US,en;q=0.8' \
* -H 'cache-control: no-cache' \
* -H 'content-type: application/x-www-form-urlencoded; charset=UTF-8' \
* -H 'cookie: mapLang=com; selectedPlantId=2467181; loginPage=login; lang=en; memoryDeviceType=%5B%7B%22key%22%3A%222467181%22%2C%22value%22%3A%22tlx%22%7D%5D; memoryDeviceSn=%5B%7B%22key%22%3A%222467181%22%2C%22value%22%3A%22tlx%25XXJDD430T8%22%7D%5D; JSESSIONID=9CFEC8AD48277631213984B4FC7DA534; assToken=faba82a846fd508c46eb25fb41cd2d9b; selPageThree=%2Fdevice%2FgetDatalogPage; selPageTwo=%2Fset%2FgetSelfInfoPage; selPage=%2Fpanel; SERVERID=0b5728179ef2ed8392dd84e289fd04cc|1707573038|1707482393' \
* -H 'origin: https://server.growatt.com' \
* -H 'pragma: no-cache' \
* -H 'referer: https://server.growatt.com/index' \
* -H 'sec-ch-ua: "Not A(Brand";v="99", "Brave";v="121", "Chromium";v="121"' \
* -H 'sec-ch-ua-mobile: ?0' \
* -H 'sec-ch-ua-platform: "Linux"' \
* -H 'sec-fetch-dest: empty' \
* -H 'sec-fetch-mode: cors' \
* -H 'sec-fetch-site: same-origin' \
* -H 'sec-gpc: 1' \
* -H 'user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36' \
* -H 'x-requested-with: XMLHttpRequest' \
* --data -raw 'tlxSn=XXJDD430T8' \
* --compressed
 *
 */

