package prv.irony.java_api.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import prv.irony.java_api.annotation.IronySession;
import prv.irony.java_api.data.session.MySession;
import reactor.core.publisher.Mono;

@RestController
public class Index {
  private Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping()
  public Mono<String> index(HttpServletRequest req, @IronySession MySession session){
    log.info("[GET] /");

    // log.info(req.getSession().getId() +"");

    return  Mono.just("hello spring!");
  }
}
