package com.osipalli.template.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.osipalli.template.common.web.Result;
import com.osipalli.template.gateway.config.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Order
@RefreshScope
@Slf4j
public class AuthenticateFilter implements GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        log.info("custom global filter");
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;
//    }
    @Autowired
    private JwtConfig jwtConfig;

    @Resource
    private RedisUtils redisUtils;

    private static final String TOKEN_NAME = "authorization";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();
        log.debug("gateway---------------"+path);
        if(log.isDebugEnabled()){
            log.debug("The requested path is:{}", path);
        }

        AtomicReference<Boolean> aBoolean = new AtomicReference<>(false);
        if (!CollectionUtils.isEmpty(jwtConfig.getWhitelist())){
            jwtConfig.getWhitelist().forEach(list -> {
                if(path.contains(list)){
                    aBoolean.set(true);
                }
            });
        }

        List<String> tokens = exchange.getRequest().getHeaders().get(TOKEN_NAME);

        if(tokens == null || tokens.isEmpty()){
            if(aBoolean.get()){
                log.debug("Allowed in the whitelist");
                String token = getToken();
                if(StringUtils.isEmpty(token)){
                    return setUnauthorizedResponse(exchange, "token exception");
                }
            }
        }

        return null;
    }

    private String getToken() {
        try {
            String key = "temporary_token";
            Object o = redisUtils.get(key);
            if (o != null) {
                return String.valueOf(o);
            }
            //Set the requested URL and credential information
            URI uri = new URI(jwtConfig.getIssuerUri() + "/oauth2/token");
            String clientId = "client-id";
            String clientSecret = "secret";
            String requestBody = "grant_type=client_credentials";
            String encodedCredentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(uri);
            post.setHeader(org.apache.http.HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);
            post.setHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
            post.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseString = EntityUtils.toString(entity);
                JSONObject jsonObject = JSONObject.parseObject(responseString);
                String token = jsonObject.getString("access_token");
                redisUtils.set(key, token, 2500000);
                return token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[Authentication exception handling]request path:{}", exchange.getRequest().getPath());
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return webFluxResponseWriter(response, msg, 4000);
    }

    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value, int code) {
        return webFluxResponseWriter(response, HttpStatus.OK, value, code);
    }

    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus status, Object value, int code) {
        return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, value, code);
    }

    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus status, Object value, int code) {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        Result result = Result.fail(code + "", value.toString());
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE+2;
    }
}
