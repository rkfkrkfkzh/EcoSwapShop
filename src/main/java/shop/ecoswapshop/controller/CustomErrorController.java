package shop.ecoswapshop.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // 에러 처리 로직을 추가하거나, 원하는 뷰를 리턴할 수 있습니다.
        return "error"; // error.html과 같은 뷰 파일을 생성하여 사용하세요.
    }

    public String getErrorPath() {
        return "/error";
    }
}

