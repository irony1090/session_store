package prv.irony.java_api.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import prv.irony.java_api.exception.ThrowingConsumer;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class HttpConfig {
  private Logger log = LoggerFactory.getLogger(getClass());

  /** https://medium.com/@odysseymoon/spring-webclient-%EC%82%AC%EC%9A%A9%EB%B2%95-5f92d295edc0
   * 
   * @return
   */
  @Bean
  public WebClient webClient(){
    // log.info("hello~");
    /**codec in memory buffer 값이 기본이 256KB -> 10MB 변경 
     * */
    ExchangeStrategies exchangeStrategies = 
      ExchangeStrategies.builder()
                        .codecs( configurer -> 
                          configurer.defaultCodecs().maxInMemorySize(1024*1024*10)
                        )
                        .build();

    /** webClient의 로깅 처리
     */
    exchangeStrategies
      .messageWriters().stream()
      .filter(LoggingCodecSupport.class::isInstance)
      .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));
    
    return WebClient.builder()
      .clientConnector(
        new ReactorClientHttpConnector(
          HttpClient
            .create()
            .secure(
              ThrowingConsumer.unchecked(
                sslContextSpec -> sslContextSpec.sslContext(
                    SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
                )
              )
            )
            .tcpConfiguration(
              client -> 
                client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120_000)
                .doOnConnected(conn -> 
                  conn.addHandlerLast(new ReadTimeoutHandler(5))
                  .addHandlerLast(new WriteTimeoutHandler(5))
                )
            )
        )
      )
      .exchangeStrategies(exchangeStrategies)
      .filter(ExchangeFilterFunction.ofRequestProcessor(
          clientRequest -> {
              log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
              clientRequest.headers()
              .forEach((name, values) -> 
                values.forEach(value -> log.debug("{} : {}", name, value))
              );
              return Mono.just(clientRequest);
          }
      ))
      .filter(ExchangeFilterFunction.ofResponseProcessor(
          clientResponse -> {
              clientResponse.headers().asHttpHeaders()
              .forEach((name, values) -> 
                values.forEach(value -> log.debug("{} : {}", name, value))
              );
              return Mono.just(clientResponse);
          }
      ))
      .build();
  }
}
