package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.client.RestTemplate;



import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import io.prometheus.client.Counter;



@EnableCircuitBreaker
@RestController
@SpringBootApplication
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
public class ReadingApplication {



    private static final Counter requestTotal = Counter.build()
        .name("ReadingApplication")
        .labelNames("method","result")
        .help("A simple Counter to illustrate custom Counters in Spring Boot and Prometheus").register();


  @Autowired
  private BookService bookService;

  @Bean
  public RestTemplate rest(RestTemplateBuilder builder) {
    return builder.build();
  }

  @RequestMapping("/to-read")
  public String toRead() {
    requestTotal.labels("toRead", "success").inc();
    return bookService.readingList();
  }


  public static void main(String[] args) {
    SpringApplication.run(ReadingApplication.class, args);

  }
}


