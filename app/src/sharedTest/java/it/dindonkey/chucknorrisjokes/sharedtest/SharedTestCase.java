package it.dindonkey.chucknorrisjokes.sharedtest;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import it.dindonkey.chucknorrisjokes.data.Joke;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class SharedTestCase
{
    protected static final List<Joke> TEST_JOKES = Collections.singletonList(new Joke(1,
            "test joke"));

    protected MockWebServer mMockWebServer;

    @SuppressWarnings("SameParameterValue")
    protected void mockJsonHttpResponse(String jsonPath) throws IOException
    {
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(jsonPath)));
    }

    private String getStringFromFile(String path) throws IOException
    {
        return IOUtils.toString(getClass().getClassLoader().getResource(path), "UTF-8");
    }
}
