package com.otzg;

import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.stereotype.Component;

/**
 * 配合react项目
 * 解决404问题
 * @return
 */

@Component
public class ErrorPageComponent implements ErrorPageRegistrar {
   @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
//        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/");
//        registry.addErrorPages(error404Page);
    }
}