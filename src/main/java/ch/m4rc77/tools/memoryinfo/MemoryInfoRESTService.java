package ch.m4rc77.tools.memoryinfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
@Produces({"application/json;charset=UTF-8"})
@Stateless
public class MemoryInfoRESTService {

    private static final Logger LOG = Logger.getLogger(MemoryInfoRESTService.class.getName());

    @Inject
    private MemoryInfoService memoryInfoService;

    @GET
    @Path("json")
    public Response getMemoryInfoJSON() {
        try {
            if (memoryInfoService == null) {
                LOG.warning("memoryInfoService not initialized ... is CDI working? " +
                        "This may happen for example if deployed in keycloak. " +
                        "Create new instance of MemoryInfoService");
                memoryInfoService = new MemoryInfoService();
            }

            MemoryInfo info = memoryInfoService.getMemoryInfo();
            return Response.ok(info).build();
        } catch (Exception e) {
            MemoryInfo info = getErrorInfo(e);
            return Response.serverError().entity(info).build();
        }
    }

    private MemoryInfo getErrorInfo(Exception e) {
        MemoryInfo errInfo = new MemoryInfo();
        errInfo.setStatus(MemoryInfoService.NOK);
        errInfo.setStatusInfo("500 internal error: Error: " + e.toString() + "\n\n" +
                "Message: " + e.getMessage() + "\n\n" +
                "Cause: " + e.getCause() + "\n\n" +
                "Stacktrace: \n" + getStacktrace(e));
        return errInfo;
    }

    private String getStacktrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
