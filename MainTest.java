package org.tests;

import com.google.gson.JsonArray;
import org.testng.annotations.Test;


public class MainTest {

    @Test
    public void getCommentTest() {
        final String RESOURCE = "/comments";
        JsonArray comments = new TestClient().getComments(RESOURCE);
        //Get the count of comments with the .com domain
    }
}
