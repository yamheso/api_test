package org.tests;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import okhttp3.Request;

public class TestClient extends BaseClient {
    public final String URL = "https://jsonplaceholder.typicode.com";

    public Object buildRequest(String apiResource, BaseRequest.RequestMethod methodType) {
        BaseRequest request = makeRequest(methodType, URL, apiResource);
        Request fullRequest = createRequest(setRequestBody(request));
        return execute(fullRequest);
    }
    public JsonArray getComments(String apiResource) {
        return sendGetRequest(URL, apiResource);
    }
}
