package org.yctech;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/20
 * Time: 17:07
 */
@Controller
public class HelloController {

    @RequestMapping("/")
    public String index(ModelMap model){
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }


}
