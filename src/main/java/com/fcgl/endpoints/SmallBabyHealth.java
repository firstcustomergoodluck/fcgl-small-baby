package com.fcgl.endpoints;

//@RequestController Is used for Spring MVC to handle web request

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RequestMaping maps / to the health() method


/**
 * Produces a simple endpoint to check that the server is alive
 */
@RestController
public class SmallBabyHealth {

  @RequestMapping("/googoo")
  public ResponseEntity health() {
    return ResponseEntity.status(200).body("gaga!\n");
  }
}
