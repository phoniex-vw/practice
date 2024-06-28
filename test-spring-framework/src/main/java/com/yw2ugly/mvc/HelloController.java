package com.yw2ugly.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
public class HelloController {

    @RequestMapping("/t1")
    public void t1(HttpServletResponse response) throws IOException {
        response.getWriter().write("hello t1()");
        log.info("t1()");
    }

    @RequestMapping("/t2")
    public void t2(){
        log.info("t2()");
    }
}
