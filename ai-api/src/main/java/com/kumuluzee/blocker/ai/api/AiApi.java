package com.kumuluzee.blocker.ai.api;

import javax.ws.rs.*;

@Path("/ai")
public class AiApi {
    @GET
    public String getResources() {
        return "Hellow world! <br> I am intelligent.";
    }
}
