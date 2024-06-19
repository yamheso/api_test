package org.tests;

import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.Util;

import java.util.Objects;

public class BaseRequest extends Request.Builder {

    private final String endpoint;
    private final String resource;
    private final RequestMethod method;
    private RequestBody requestBody;

    protected BaseRequest(RequestMethod method, String endpoint, String resource) {
        this.method = method;
        this.endpoint = endpoint;
        this.resource = resource;
    }

    protected Request getRequest() {
        switch (method) {
            case GET -> get();
            case PUT -> put(Objects.requireNonNull(requestBody, "The request body is required for the PUT method!"));
            case POST -> post(Objects.requireNonNull(requestBody, "The request body is required for the POST method"));
            case DELETE -> delete(Objects.isNull(requestBody) ? Util.EMPTY_REQUEST : requestBody);
            default -> throw new RuntimeException("The API method is not defined");
        }
        return url(endpoint + resource).build();
    }
    protected BaseRequest createRequestBody(JsonObject jsonRequestBody) {
        requestBody = RequestBody.create(MediaType.parse("application/json"), jsonRequestBody.toString());
        return this;
    }
    protected enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE
    }
}