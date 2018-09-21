package com.qydcos.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.qydcos.common.ResultCode.FILE_SIZE_TOO_BIG;

/**
 * @author AlanFan
 */
@RestControllerAdvice
public class QYExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    MultipartProperties multipartProperties;

    @ExceptionHandler({QYException.class, MultipartException.class})
    @ResponseBody
    ResponseEntity<Object> handleInvalidRequest(Exception e, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        if (e instanceof QYException) {
            return handleExceptionInternal(e, ((QYException) e).getError(), headers, HttpStatus.OK, request);
        } else {
            ApiResult apiResult = new ApiResult();
            apiResult.setResultCode(FILE_SIZE_TOO_BIG);
            apiResult.setData(String.format("文件最大为%s", multipartProperties.getMaxFileSize()));
            System.out.println(e.getMessage());
            return handleExceptionInternal(e, apiResult, headers, HttpStatus.OK, request);
        }
    }
}
