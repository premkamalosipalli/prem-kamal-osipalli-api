package com.spting.auth.config;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TokenUtil {

    @Autowired
    public JwtConfig jwtConfig;

    public String getToken(){
        try{
            System.out.println("Token Class");
            URI uri = new URI(jwtConfig.getIssuerUri()+"/oauth/token");
            String clientId = "client-credentials";
            String clientSecret = "Black123";
            String requestBody = "grant_type=client_credentials";
            String encodedCredentials = Base64.getEncoder().encodeToString((clientId+":"+clientSecret).getBytes(StandardCharsets.UTF_8));

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(uri);
            post.setHeader(org.apache.http.HttpHeaders.AUTHORIZATION, "Bearer " + encodedCredentials);
            post.setHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
            post.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseString = EntityUtils.toString(entity);
                JSONObject jsonObject = JSONObject.parseObject(responseString);
                String token = jsonObject.getString("access_token");
                return token;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
