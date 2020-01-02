package com.kumuluzee.blocker.ai.api;

import com.kumuluz.ee.discovery.annotations.DiscoverService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Optional;

@Path("/ai")
public class AiApi {
    @Inject
    @DiscoverService(value = "ai", environment = "test", version = "1.0.0")
    Optional<String> aiUrlString;

    @Inject
    @DiscoverService(value = "provider", environment = "test", version = "1.0.0")
    Optional<String> providerUrlString;

    @GET
    @Path("/integrations")
    public String integrations(){
        return "ai: " + aiUrlString.toString() + "\n" +
                "provider: " + providerUrlString.toString() + "\n";
    }

    @GET
    public String getResources() {
        return "Hellow world! <br> I am intelligent.";
    }


}
