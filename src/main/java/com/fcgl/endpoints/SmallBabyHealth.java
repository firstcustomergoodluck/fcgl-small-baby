package com.fcgl.endpoints;

//@RequestController Is used for Spring MVC to handle web request

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Produces a simple endpoint to check that the server is alive
 */
@RestController
public class SmallBabyHealth {

  @RequestMapping("/googoo")
  public ResponseEntity health() {
    Logger logger = LogManager.getLogger("SmallBabyHealth");
    logger.info("GA GA!!!");
    return ResponseEntity.status(200).body("gaga!\n");
  }
}
