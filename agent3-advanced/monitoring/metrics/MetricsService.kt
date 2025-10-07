package com.spiritscribe.monitoring.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class MetricsService(
    private val meterRegistry: MeterRegistry
) {
    
    fun incrementApiCall(endpoint: String, status: String) {
        Counter.builder("api.calls")
            .tag("endpoint", endpoint)
            .tag("status", status)
            .register(meterRegistry)
            .increment()
    }
    
    fun recordResponseTime(endpoint: String, duration: Duration) {
        Timer.builder("api.response.time")
            .tag("endpoint", endpoint)
            .register(meterRegistry)
            .record(duration)
    }
}
