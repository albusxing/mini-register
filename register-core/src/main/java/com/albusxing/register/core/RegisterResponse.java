package com.albusxing.register.core;

import lombok.Data;

/**
 * 注册响应对象
 * @author liguoqing
 */
@Data
public class RegisterResponse {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    /**
     * 注册返回状态：success、failure
     */
    private String status;

}
