package com.inretailpharma.digital.ordertracker.config.aditional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import java.lang.reflect.Type;

public class AccessTokenAdapter implements JsonSerializer<DefaultOAuth2AccessToken> {

    @Override
    public JsonElement serialize(DefaultOAuth2AccessToken accessToken, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject tokenInfo = new JsonObject();
        tokenInfo.addProperty("access_token", accessToken.getValue());
        tokenInfo.addProperty("expiresIn", accessToken.getExpiresIn());
        tokenInfo.addProperty("tokenType", accessToken.getTokenType());
        tokenInfo.addProperty("validated", (Boolean) accessToken.getAdditionalInformation().get("validated"));
        tokenInfo.addProperty("nick_name", (String) accessToken.getAdditionalInformation().get("nick_name"));
        tokenInfo.addProperty("name", (String) accessToken.getAdditionalInformation().get("name"));
        tokenInfo.addProperty("cellphone", (String) accessToken.getAdditionalInformation().get("cellphone"));
        tokenInfo.addProperty("email", (String) accessToken.getAdditionalInformation().get("email"));
        tokenInfo.addProperty("type_motorized", (String) accessToken.getAdditionalInformation().get("type_motorized"));
        tokenInfo.addProperty("enabled", (Boolean) accessToken.getAdditionalInformation().get("enabled"));
        tokenInfo.addProperty("uid", (String) accessToken.getAdditionalInformation().get("userId"));
        tokenInfo.addProperty("dni", (String) accessToken.getAdditionalInformation().get("dni"));
        tokenInfo.addProperty("local_code", (String) accessToken.getAdditionalInformation().get("local"));
        return tokenInfo;
    }
}
