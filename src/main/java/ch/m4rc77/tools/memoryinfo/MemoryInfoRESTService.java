package ch.m4rc77.tools.memoryinfo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
@Produces({ "application/json;charset=UTF-8" })
public class MemoryInfoRESTService {

    @Inject
    private MemoryInfoService memoryInfoService;

    @GET
    @Path("json")
    public Response getHelloWorldJSON() {
        MemoryInfo info = memoryInfoService.getMemoryInfo();
        return Response.ok(info).build();
    }

}
