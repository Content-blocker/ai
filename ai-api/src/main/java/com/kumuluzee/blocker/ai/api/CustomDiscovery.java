package com.kumuluzee.blocker.ai.api;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.json.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class CustomDiscovery {
    public static Optional<WebTarget> discover(String service, String environment, String version) {
        String etcd = ConfigurationUtil.getInstance().get("kumuluzee.discovery.etcd.hosts").get();
        Client client = ClientBuilder.newBuilder()
                .property("connection.timeout", 100)
                .register(JacksonJsonProvider.class)
                .build();
        WebTarget etcdTarget = client.target(etcd);
        WebTarget pathTarget = etcdTarget.path("/v2/keys/environments/" + environment + "/services/" + service + "/" + version + "/instances");
        try {
            Response response = pathTarget.request().get();
            String responseString = response.readEntity(String.class);
            JSONObject responseJSON = new JSONObject(responseString);
            String instanceKey = responseJSON.getJSONObject("node").getJSONArray("nodes").getJSONObject(0).get("key").toString();
            pathTarget = etcdTarget.path("/v2/keys" + instanceKey + "/url");
        } catch (Exception e) {
            System.out.println(e.toString());
            return Optional.empty();
        }
        String url = "";
        try {
            Response response = pathTarget.request().get();
            String responseString = response.readEntity(String.class);
            JSONObject responseJSON = new JSONObject(responseString);
            url = responseJSON.getJSONObject("node").get("value").toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            return Optional.empty();
        }

        WebTarget target = client.target(url);

        return Optional.of(target);
    }
}