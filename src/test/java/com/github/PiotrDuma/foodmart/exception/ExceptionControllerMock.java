package com.github.PiotrDuma.foodmart.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestController
public class ExceptionControllerMock {

  @RequestMapping("/test/path")
  public void callPathNotFoundException() throws Exception{
    throw new NoHandlerFoundException("GET", "localhost:8080/path", HttpHeaders.EMPTY);
  }

  @RequestMapping("/test/runtime")
  public void callRuntimeException() throws RuntimeException{
    throw new RuntimeException("Something went wrong");
  }

  @RequestMapping("/test/resource")
  public void callResourceNotFoundException() throws RuntimeException{
    throw new ResourceNotFoundException("Something is not found");
  }
}
