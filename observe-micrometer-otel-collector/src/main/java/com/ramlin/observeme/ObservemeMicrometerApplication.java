package com.ramlin.observeme;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Optional;

@SpringBootApplication
public class ObservemeMicrometerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ObservemeMicrometerApplication.class, args);
	}

/*	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ObservemeMicrometerApplication.class);
	}*/

	@Bean
	@ConditionalOnClass(name = "io.opentelemetry.javaagent.OpenTelemetryAgent")
	public MeterRegistry otelRegistry() {
		Optional<MeterRegistry> otelRegistry = Metrics.globalRegistry.getRegistries().stream()
				.filter(r -> r.getClass().getName().contains("OpenTelemetryMeterRegistry"))
				.findAny();
		otelRegistry.ifPresent(Metrics.globalRegistry::remove);
		return otelRegistry.orElse(null);
	}

	@Bean
	public LoggingMeterRegistry loggingMeterRegistry() {

		LoggingRegistryConfig config = new LoggingRegistryConfig() {

			@Override
			public String get(String s) {

				return null;
			}

			@Override
			public Duration step() {

				return Duration.ofMinutes(2);
			}
		};
		LoggingMeterRegistry registry = LoggingMeterRegistry.builder(config).clock(Clock.SYSTEM).threadFactory(new NamedThreadFactory("logging-metrics-publisher")).build();
		registry.config()
				.meterFilter(MeterFilter.ignoreTags("too.much.information"))
				.meterFilter(MeterFilter.denyNameStartsWith("jvm"))
				.meterFilter(MeterFilter.denyNameStartsWith("hikaricp"))
				.meterFilter(MeterFilter.denyNameStartsWith("jdbc"))
				.meterFilter(MeterFilter.denyNameStartsWith("system"))
				.meterFilter(MeterFilter.denyNameStartsWith("process"))
				.meterFilter(MeterFilter.denyNameStartsWith("tomcat"))
				.meterFilter(MeterFilter.denyNameStartsWith("application"))
				.meterFilter(MeterFilter.denyNameStartsWith("disk"))
				.meterFilter(MeterFilter.denyNameStartsWith("executor"));
		return registry;
	}

}
