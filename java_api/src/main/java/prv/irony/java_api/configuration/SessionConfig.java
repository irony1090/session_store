package prv.irony.java_api.configuration;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.reactive.function.client.WebClient;

import prv.irony.java_api.data.session.MySession;

@Configuration
@EnableSpringHttpSession
public class SessionConfig {
  private Logger log = LoggerFactory.getLogger(getClass());
  @Autowired private WebClient webClient;

  @Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookieName("Nsess"); 
		// serializer.setCookiePath("/"); 
		// serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); 
		return serializer;
	}
  @Bean
  public SessionRepository<MySession> sessionRepository(){
    return new SessionRepository<MySession>(){

    @Override
		public MySession createSession() {
      var monoBody = 
        webClient.mutate()
                 .build()
                 .get()
                 .uri("http://localhost:3090/")
                 .retrieve()
                 .bodyToMono(HashMap.class);

			var body = monoBody.block();
			System.out.println(body);

      // {session={cookie={originalMaxAge=null, expires=null, httpOnly=true, path=/}}, sessionID=Jez0QWQmXXLpeSNa94uWB-hJ6c2ra8Ss}

			return new MySession( (String) body.get("sessionID") );
		}

		@Override
		public void save(MySession session) {
			
		}

		@Override
		public MySession findById(String id) {
			log.info("[FIND] : " + id);
			return null;
		}

		@Override
		public void deleteById(String id) {
			
		}
      
    };
  }
}
