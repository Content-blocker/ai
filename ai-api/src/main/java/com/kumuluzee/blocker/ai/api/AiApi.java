package com.kumuluzee.blocker.ai.api;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.discovery.enums.AccessType;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Log
@RequestScoped
@Path("/api")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class AiApi {
    static Optional<WebTarget> aiTarget = CustomDiscovery.discover("ai", "test", "1.0.0");
    static Optional<WebTarget> providerTarget = CustomDiscovery.discover("provider", "test", "1.0.0");
    static Optional<WebTarget> fetcherTarget = CustomDiscovery.discover("fetcher", "test", "1.0.0");
    static String aiString = (aiTarget.isPresent() ? aiTarget.get().getUri().toString() : "Empty");
    static String providerString = (providerTarget.isPresent() ? providerTarget.get().getUri().toString() : "Empty");
    static String fetcherString = (fetcherTarget.isPresent() ? fetcherTarget.get().getUri().toString() : "Empty");

    static PythonHandler pythonHandler = new PythonHandler();

    @GET
    @Timed
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getResources() {
        String links = "";
        if(aiTarget.isPresent()){
            links += "<a href='"+ aiString + "/ai/api/integrations'>ai/api/integrations</a><br>";
            links += "<a href='"+ aiString + "/ai/health'>ai/health</a><br>";
            links += "<a href='"+ aiString + "/ai/api/block'>ai/api/block</a><br>";
        }
        return "Hellow world! <br> I am intelligent. <br><br>" + links;
    }

    @GET
    @Timed
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/integrations")
    public String integrations() {
        String out = "<body> <h5> ai integrations </h5>";

        out += "<div> ai: ";
        if (aiTarget.isPresent()) {
            out += "ai: " + aiString + "<br>";
            out += "<a href='"+ aiString + "/ai/api'>ai/api</a><br>";
        }
        else out+= "missing <br>";
        out += "</div>";

        out += "<div> provider: ";
        if (aiTarget.isPresent()) {
            out += "provider: " + providerString + "<br>";
            out += "<a href='"+ providerString + "/provider/api'>provider/api</a><br>";
        }
        else out+= "missing <br>";
        out += "</div>";

        out += "<div> fetcher: ";
        if (aiTarget.isPresent()) {
            out += "fetcher: " + fetcherString + "<br>";
            out += "<a href='"+ fetcherString + "/fetcher/api'>fetcher/api</a><br>";
        }
        else out+= "missing <br>";
        out += "</div>";

        out += "</body>";
        return out;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/block")
    public Response block() {
        return Response
                .status(Response.Status.OK)
                .entity("[{\"block\":\"ocean\"}]")
                .build();
    }

    private Response test_python_app(Double sensitivity, int test){
        String[] splitIn = new String[]{};
        int[] expected = new int[]{};
        if(test == 1){
            splitIn = new String[]{
                    "Everyone meets in King's Landing to discuss the fate of the realm",
                    "In Winterfell, Sansa confronts Arya",
                    "Sam reaches Winterfell, where he and Bran discover a shocking secret about Jon Snow",
                    "Arya kills Littlefinger",
                    "Game of Thrones 7x06 - Army of the Dead attacks Jon Snow and his men",
                    "Kit Harington's Thoughts On Jon Snow's Ending!? - Game of Thrones Season 8 (Ending)",
                    "Game of Thrones Cast React to Season 8 at Final Table Read (Full Version)",
                    "Random text put here to confuse",
                    "And another writing that is in this place for absolutely no good reason, #spam",
                    "asonaosfjasjfpasp asiasjf asofjasf ojiq ojp",
                    "Live updates: Federal officers confront protesters outside White House with tear gas; Bowser condemns federal agencies’ actions"
            };
            expected = new int[]{1,1,1,1,1,0,0,0,0,0,0};
        }
        if(test > 1){
            splitIn = new String[]{
                    "The wight is presented to the Lannister court",
                    "Why did the Night's Watch kill Jon Snow",
                    "Upon reaching Winterfell with their combined armies, Jon and Daenerys learn the Army of the Dead has breached the Wall, and the Night King commands the undead Viserion",
                    "Jaime arrives at Winterfell where Bran awaits him",
                    "Nine noble families wage war against each other in order to gain control over the mythical land of Westeros.",
                    "Arya took a stroll in the park today",
                    "Game of thrones is a great show with dragons, except the last season",
                    "Dungeons and dragons is a fun game you can play with your friends, but don't die lvl 1"
            };
            expected = new int[]{1,1,1,1,0,0,0,0};
        }

        String returnStr ="";
        for(int i=0; i< splitIn.length; ++i){
            if(i!=0) returnStr += "\n";
            String str = splitIn[i].replaceAll("[^a-zA-Z'\\s]", "");
            String result = pythonHandler.pipe(str);
            String[] avgmax= result.split(",");
            Double avg = Double.parseDouble(avgmax[0]);
            Double max = Double.parseDouble(avgmax[1]);
            int qr = max > sensitivity ? 1 : 0;
            returnStr += expected[i] + "  " + qr + "  " + max + "  " + str;
        }

        return Response
                .status(Response.Status.OK)
                .entity(returnStr)
                .build();
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/python-app")
    public Response python_app(@QueryParam("input") String input, @QueryParam("sensitivity") String sensitivity, @QueryParam("test") String test) {
        String[] splitIn = input.split("\\|");
        Double isensitivity = Double.valueOf(sensitivity)/100;
        if(Integer.parseInt(test) >0 ){
            return test_python_app(isensitivity, Integer.parseInt(test));
        }


        String returnStr ="";
        for(int i=0; i< splitIn.length; ++i){
            if(i!=0) returnStr += "|";
            returnStr += pythonHandler.pipe(splitIn[i]);
        }
        return Response
                .status(Response.Status.OK)
                .entity(returnStr)
                .build();
    }
}
