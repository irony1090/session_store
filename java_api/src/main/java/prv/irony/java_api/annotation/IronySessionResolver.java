package prv.irony.java_api.annotation;


import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import prv.irony.java_api.data.session.SessionWrap;


@Component
@ControllerAdvice
public class IronySessionResolver 
implements HandlerMethodArgumentResolver, ResponseBodyAdvice<Object>{
  private Logger log = LoggerFactory.getLogger(getClass());

  @Autowired private WebClient webClient;
  private String SET_COOKIE = "Set-Cookie";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    log.info("supportsParameter");

    return true;
  }
  @Override
  public Object resolveArgument(
    MethodParameter parameter, ModelAndViewContainer mavContainer,
    NativeWebRequest webRequest, WebDataBinderFactory binderFactory
  ) throws Exception {

    HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);
    HttpServletResponse res = webRequest.getNativeResponse(HttpServletResponse.class);

    Iterator<String> headerNames = req.getHeaderNames().asIterator();

    ClientResponse clientResponse =
    webClient.mutate().build().get()
      .uri("/")
      .headers(httpHeaders -> {
        while(headerNames.hasNext()){
          String name = headerNames.next();
          String val = req.getHeader(name);
          httpHeaders.set(name, val);
          // System.out.println(name+ " : " + val);
        }
      })
      .exchange().block();
    // var body = clientResponse.bodyToMono(HashMap.class).block();
    SessionWrap body = clientResponse.bodyToMono(SessionWrap.class).block();
    HttpHeaders clientHeader = clientResponse.headers().asHttpHeaders();

    Iterator<String> clientHeadersNames = clientHeader.keySet().iterator();
    // System.out.println("클라이언트 헤더");
    // Cookie nsessCookie = this.getCookie(req, "Nsess");
    // System.out.println(nsessCookie != null ? nsessCookie.getValue() : null);
    while(clientHeadersNames.hasNext()){
      String name = clientHeadersNames.next();
      String val = clientHeader.getFirst(name);
      // System.out.println(name + " : " + val);
      if(SET_COOKIE.equalsIgnoreCase(name))
        res.setHeader(SET_COOKIE, val);
      
    }
    log.info("BODY : " + body);
    // {cookie={originalMaxAge=60, expires=1602144713965, httpOnly=true, path=/}, session={}, sessionID=wtNLOzyQLdjWHSKNz0f3hb_m80LMca_U}


    return null;
  }

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
	@Override
	public Object beforeBodyWrite(
    Object body, MethodParameter returnType, MediaType selectedContentType,
    Class<? extends HttpMessageConverter<?>> selectedConverterType, 
    ServerHttpRequest request, ServerHttpResponse response
  ) {

    log.info("beforeBodyWrite");
		return body;
  }

  
  public Cookie getCookie(HttpServletRequest req, String key){
    Cookie[] cookies = req.getCookies();

    if(cookies == null) return null;

    for(int i=0; i<cookies.length; i++){
      Cookie cookie = cookies[i];
      if(cookie.getName().equals(key))
        return cookie;
    }
    return null;
  }
  
}
