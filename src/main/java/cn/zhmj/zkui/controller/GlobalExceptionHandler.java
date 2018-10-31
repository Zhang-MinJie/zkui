package cn.zhmj.zkui.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public String jsonErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("[业务异常] - {}", e.getMessage(), e);
        request.setAttribute("errorMsg", e);
        return "error";
    }
}
