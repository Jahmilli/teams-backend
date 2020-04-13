package ses.teamsbackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/test")
public class TestController {

  @GetMapping("/test")
  public String getString() {
    return "This is a string";
  }
}
