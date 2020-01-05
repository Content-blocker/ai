package com.kumuluzee.blocker.ai.api;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.xml.ws.Response;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService(value = "ai", ttl = 40, pingInterval = 20, environment = "test", version = "1.0.0")
@ApplicationPath("/ai")
public class RestApplication extends Application {

}
