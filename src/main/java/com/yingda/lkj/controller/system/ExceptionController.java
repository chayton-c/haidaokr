package com.yingda.lkj.controller.system;

import com.yingda.lkj.beans.exception.CustomException;
import com.yingda.lkj.beans.system.Json;
import com.yingda.lkj.beans.system.JsonMessage;
import com.yingda.lkj.controller.BaseController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 全局异常处理
 *
 * @author hood  2019/12/16
 */
@RestControllerAdvice
public class ExceptionController extends BaseController {

    private static final String AUTH_ERROR_PATH = "forward:/auth/loginPage";
    private static final String ERROR_PATH = "error";

    // 数据一致性
    @ResponseBody
    @ExceptionHandler(JpaSystemException.class)
    public Object handleJpaSystemException(JpaSystemException exception) {
        logger.error("error in " + req.getRequestURI(), exception);
        Throwable rootCause = exception.getRootCause();
        if (rootCause == null)
            return new Json(JsonMessage.PARAM_INVALID, "网络繁忙，请稍后再试");

        String message = rootCause.getMessage();
        try {
            message = message.substring(message.indexOf("for key ") + 8);
        } catch (Exception e) {
            return new Json(JsonMessage.PARAM_INVALID, rootCause.getMessage());
        }
        return new Json(JsonMessage.PARAM_INVALID, message);
    }
    // 数据一致性
    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        logger.error("error in " + req.getRequestURI(), exception);
        Throwable rootCause = exception.getRootCause();
        if (rootCause == null)
            return new Json(JsonMessage.PARAM_INVALID, "网络繁忙，请稍后再试");

        String message = rootCause.getMessage();
        try {
            message = message.substring(message.indexOf("for key ") + 8);
        } catch (Exception e) {
            return new Json(JsonMessage.PARAM_INVALID, rootCause.getMessage());
        }
        return new Json(JsonMessage.PARAM_INVALID, message);
    }

    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public Object handleCustomException(CustomException customException) {
        if (customException.isNeedToLog())
            logger.error("error in " + req.getRequestURI(), customException);
        return customException.getJson();
    }

    // 全局异常
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleCommonException(Exception exception) {
        logger.error("error in " + req.getRequestURI(), exception);
        return new Json(JsonMessage.SYS_ERROR);
    }

    @RequestMapping("/error/{errorMsg}")
    public ModelAndView error(@PathVariable String errorMsg) {
        return new ModelAndView(ERROR_PATH, Map.of("errorMsg", errorMsg));
    }

}
