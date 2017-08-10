package hello;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.net.URI;




import io.prometheus.client.Counter;
import com.soundcloud.prometheus.hystrix.HystrixPrometheusMetricsPublisher;




@Service
public class BookService {


  private final RestTemplate restTemplate;


    private static final Counter requestTotal = Counter.build()
        .name("BookService")
        .labelNames("method","result")
        .help("A simple Counter to illustrate custom Counters in Spring Boot and Prometheus").register();


  public BookService(RestTemplate rest) {
    this.restTemplate = rest;
    HystrixPrometheusMetricsPublisher.register("BookService");
  }

  @HystrixCommand(fallbackMethod = "reliable")
  public String readingList() {
    URI uri = URI.create("http://bookstore:8090/recommended");

    String response = this.restTemplate.getForObject(uri, String.class);
    requestTotal.labels("readingList", "success").inc();
    return response;
  }

  public String reliable() {
    requestTotal.labels("reliable", "failure").inc();
    return "Cloud Native Java (O'Reilly)";
  }

}