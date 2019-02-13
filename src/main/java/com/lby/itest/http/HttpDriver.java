package com.lby.itest.http;

import com.lby.itest.contants.HttpConstants;
import com.lby.itest.utils.PropertiesUtil;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

/**
 * @Author binger
 * @CreateTime 2019.2.2
 * 基于Rest-Assured封装的http请求工具包，可通过配置文件配置http参数
 * 支持get/post请求方法
 * 支持body的序列化/非序列化
 */
public class HttpDriver {
    //增加http的配置文件
    private static String propertyFile = "src/main/resources/http/httpconf.properties";

    static {
        setRestAssuredConfig();
    }

    //Rest-Assured配置
    private static void setRestAssuredConfig() {
        String[] keyList = new String[]{
                "http.connection.timeout",
                "http.socket.timeout",
                "http.connection.manager.timeout"
        };

        Map<String, Object> httpClientParams = new HashMap<String, Object>();

        PropertiesUtil.loadProperties(propertyFile);
        for (int i = 0; i < keyList.length; i++) {
            httpClientParams.put(keyList[i], PropertiesUtil.getValue(keyList[i]));
        }

        config = RestAssured.config().httpClient(HttpClientConfig.httpClientConfig().addParams(httpClientParams));
        config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"));

        String host = PropertiesUtil.getValue("proxy.host");
        Integer port = Integer.valueOf(PropertiesUtil.getValue("proxy.port"));
        if (!StringUtils.isBlank(host) && port.equals(null)) {
            RestAssured.proxy(host, port);
        } else if (StringUtils.isBlank(host) && !port.equals(null)) {
            RestAssured.proxy(port);
        } else if (!StringUtils.isBlank(host) && port.equals(null)) {
            RestAssured.proxy(host);
        }

        RestAssured.replaceFiltersWith(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    public static Response doPostWithNothing(String path) {
        return doHttpRequest(path, HttpConstants.POST_METHOD, null, null, null, null);
    }

    public static Response doPostWithQueryParams(String path, Map<String, String> queryParams) {
        return doHttpRequest(path, HttpConstants.POST_METHOD, null, queryParams, null, null);
    }

    public static Response doPostWithHeaders(String path, Map<String, String> headers) {
        return doHttpRequest(path, HttpConstants.POST_METHOD, headers, null, null, null);
    }

    public static Response doPostWithFormParams(String path, Map<String, Object> formParams) {
        return doHttpRequest(path, HttpConstants.POST_METHOD, null, null, formParams, null);
    }

    public static Response doPostWithHeadersAndFormParams(String path, Map<String, String> headers, Map<String, Object> formParams) {
        return doHttpRequest(path, HttpConstants.POST_METHOD, headers, null, formParams, null);
    }

    public static Response doPostWithHeadersAndQueryParamsAndFormParams(String path, Map<String, String> headers, Map<String, String> queryParams, Map<String, Object> formParams) {
        return doHttpRequest(path, HttpConstants.POST_METHOD, headers, queryParams, formParams, null);
    }

    public static Response doPostWithBodyParams(String path, Object bodyParams) {
        return doHttpRequest(path, HttpConstants.POST_METHOD, null, null, null, bodyParams);
    }

    public static Response doPostWithHeadersAndBodyParams(String path, Map<String, String> headers, Object bodyParams) {
        return doHttpRequest(path, HttpConstants.POST_METHOD, headers, null, null, bodyParams);
    }

    public static Response doPostWithHeadersAndQueryParamsAndBodyParams(String path, Map<String, String> headers, Map<String, String> queryParams, Object bodyParams) {
        return doHttpRequest(path, HttpConstants.POST_METHOD, headers, queryParams, null, bodyParams);
    }

    public static Response doGetWithNothing(String path) {
        return doHttpRequest(path, HttpConstants.GET_METHOD, null, null, null, null);
    }

    public static Response doGetWithHeaders(String path, Map<String, String> headers) {
        return doHttpRequest(path, HttpConstants.GET_METHOD, headers, null, null, null);
    }

    public static Response doGetWithQueryParams(String path, Map<String, String> queryParams) {
        return doHttpRequest(path, HttpConstants.GET_METHOD, null, queryParams, null, null);
    }

    public static Response doGetWithHeadersAndParams(String path, Map<String, String> headers, Map<String, String> queryParams) {
        return doHttpRequest(path, HttpConstants.GET_METHOD, headers, queryParams, null, null);
    }

    private static Response doHttpRequest(String path, String method, Map<String, String> headers, Map<String, String> queryParams,
                                          Map<String, Object> formParams, Object body) {
        Response response = null;

        boolean isSerial = false;

        if (formParams == null && body != null) {
            isSerial = true;
        }

        if (isSerial) {
            if (headers != null && queryParams != null && formParams != null) {
                response = given().headers(headers).queryParams(queryParams).formParams(formParams).request(method, path);
            } else if (headers != null && queryParams != null && formParams == null) {
                response = given().headers(headers).queryParams(queryParams).request(method, path);
            } else if (headers != null && queryParams == null && formParams != null) {
                response = given().headers(headers).formParams(formParams).request(method, path);
            } else if (headers != null && queryParams == null && formParams == null) {
                response = given().headers(headers).request(method, path);
            } else if (headers == null && queryParams != null && formParams != null) {
                response = given().queryParams(queryParams).formParams(formParams).request(method, path);
            } else if (headers == null && queryParams != null && formParams == null) {
                response = given().queryParams(queryParams).request(method, path);
            } else if (headers == null && queryParams == null && formParams != null) {
                response = given().formParams(formParams).request(method, path);
            } else if (headers == null && queryParams == null && formParams == null) {
                response = given().request(method, path);
            }
        } else {
            if (headers != null && queryParams != null && formParams != null) {
                response = given().headers(headers).queryParams(queryParams).body(formParams).request(method, path);
            } else if (headers != null && queryParams != null && formParams == null) {
                response = given().headers(headers).queryParams(queryParams).request(method, path);
            } else if (headers != null && queryParams == null && formParams != null) {
                response = given().headers(headers).body(formParams).request(method, path);
            } else if (headers != null && queryParams == null && formParams == null) {
                response = given().headers(headers).request(method, path);
            } else if (headers == null && queryParams != null && formParams != null) {
                response = given().queryParams(queryParams).body(formParams).request(method, path);
            } else if (headers == null && queryParams != null && formParams == null) {
                response = given().queryParams(queryParams).request(method, path);
            } else if (headers == null && queryParams == null && formParams != null) {
                response = given().body(formParams).request(method, path);
            } else if (headers == null && queryParams == null && formParams == null) {
                response = given().request(method, path);
            }
        }

        return response;
    }
}
