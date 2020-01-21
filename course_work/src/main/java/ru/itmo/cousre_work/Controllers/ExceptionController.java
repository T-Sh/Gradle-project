package ru.itmo.cousre_work.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController implements ErrorController {

    private static final String PATH = "/403";

    @RequestMapping(value = PATH)
    public String error_f() {
        return "error_page";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
