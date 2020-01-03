package com.kumuluzee.blocker.ai.api;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.discovery.enums.AccessType;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@RequestScoped
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiApi {
    @Inject
    @DiscoverService(value = "ai", environment = "test", version = "1.0.0", accessType = AccessType.GATEWAY)
    Optional<String> aiUrlString;

    @Inject
    @DiscoverService(value = "provider", environment = "test", version = "1.0.0", accessType = AccessType.GATEWAY)
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
