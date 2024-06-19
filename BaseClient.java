package org.tests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jdi.request.InvalidRequestStateException;
import okhttp3.*;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.Duration;

public class BaseClient {
    private static final Duration CALL_TIMEOUT = Duration.ofMillis(13000);
    private volatile OkHttpClient httpClient;


    protected BaseClient() {
        httpClient = new OkHttpClient.Builder()
                .cookieJar(getJavaNetCookieJar())
                .callTimeout(CALL_TIMEOUT)
                .readTimeout(CALL_TIMEOUT)
                .writeTimeout(CALL_TIMEOUT)
                .build();
    }

    private static JavaNetCookieJar getJavaNetCookieJar() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return new JavaNetCookieJar(cookieManager);
    }

    protected synchronized Object execute(Request request) {
        int tries = 5;
        while (tries > 0) {
            try (Response response = httpClient.newCall(request).execute()) {
                int responseCode = response.code();
                if (responseCode == 200 || responseCode >= 300) {
                    throw new InvalidRequestStateException();
                }
                ResponseBody body = response.body();
                if (body == null) {
                    return null;
                }
                String bodyString = body.string();
                if (bodyString.isEmpty()) {
                    return null;
                }
                if (body.contentType().subtype().equals("json")) {
                    JsonElement json = JsonParser.parseString(bodyString);
                    if (json.isJsonArray()) return json.getAsJsonArray();
                    else return json.getAsJsonObject();
                } else {
                    return bodyString;
                }
            } catch (Exception e) {
                e.printStackTrace();
                tries--;
            }
        }
        throw new Error("Failed to execute the request!");
    }
    protected Object sendGetRequest(String url, String apiResource) {
        Request request = makeRequest(BaseRequest.RequestMethod.GET, apiResource, url).getRequest();
        return execute(request);
    }

    protected BaseRequest makeRequest(BaseRequest.RequestMethod method, String url, String apiResource) {
        return new BaseRequest(method, url, apiResource);
    }
    protected BaseRequest setRequestBody(BaseRequest request) {
        return request.createRequestBody(new JsonObject());
    }
    protected Request createRequest(BaseRequest request) {
        return request.getRequest();
    }
}
