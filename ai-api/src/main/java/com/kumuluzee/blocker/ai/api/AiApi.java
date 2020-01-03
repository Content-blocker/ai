package com.kumuluzee.blocker.ai.api;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.discovery.enums.AccessType;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@RequestScoped
@Path("/api")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class AiApi {
    static Optional<WebTarget> aiTarget = CustomDiscovery.discover("ai", "test", "1.0.0");
    static Optional<WebTarget> providerTarget = CustomDiscovery.discover("provider", "test", "1.0.0");
    static String aiString = aiTarget.get().getUri().toString();
    static String providerString = providerTarget.get().getUri().toString();

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getResources() {
        String links = "";
        if(aiTarget.isPresent()){
            links += "<a href='"+ aiString + "/api/integrations'>ai/api/integrations</a><br>";
        }
        if(providerTarget.isPresent()){
            links += "<a href='"+ providerString + "/api'>provider/api</a><br>";
        }

        return "Hellow world! <br> I am intelligent. <br>" + links;
    }

    @GET
    @Path("/integrations")
    public String integrations() {
        String out = "";
        if (aiTarget.isPresent()) {
            out += "ai: " + aiString + "\n";
        }
        else out+= "missing ai \n";
        if (providerTarget.isPresent()) {
            out += "provider: " + providerString + "\n";
        }
        else out+= "missing provider \n";
        return out;
    }
}
