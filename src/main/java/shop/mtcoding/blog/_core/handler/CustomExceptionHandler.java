package shop.mtcoding.blog._core.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.mtcoding.blog._core.util.Script;

@RestControllerAdvice // 응답 에러 컨트롤러(view 리턴)
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public @ResponseBody String error1(Exception e){
        return Script.back(e.getMessage());
    }
}
