package com.kumuluzee.blocker.ai.api;

import javax.ws.rs.*;

@Path("/call")
public class AiApi {
    @GET
    public String getResources() {
        return "stringai";
    }
}
