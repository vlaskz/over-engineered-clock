package com.vlaskz.rssreader.client

import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeInfo
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class NtpClient {

    companion object {
        fun getCurrentNtpTime(timeZone: ZoneId): LocalDateTime {
            return try {
                val client = NTPUDPClient()
                client.open()

                val info: TimeInfo = client.getTime(InetAddress.getByName("pool.ntp.org"))
                val ntpTime = Instant.ofEpochMilli(info.message.transmitTimeStamp.time)

                LocalDateTime.ofInstant(ntpTime, timeZone)
            } catch (e: Exception) {
                LocalDateTime.now(timeZone)
            }
        }
    }

}