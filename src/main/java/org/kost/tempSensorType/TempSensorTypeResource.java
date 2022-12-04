package org.kost.tempSensorType;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.kost.exceptions.ServiceException;
import org.kost.manufacturer.Manufacturer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Path("/tempSensorTypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "tempSensorType", description = "TempSensorType Operations")
@AllArgsConstructor
@Slf4j
public class TempSensorTypeResource {


    private final TempSensorTypeService tempSensorTypeService;
    private AtomicLong counter = new AtomicLong(0);
    private static final Logger LOGGER = Logger.getLogger(TempSensorTypeResource.class);

    @GET
    @APIResponse(
            responseCode = "200",
            description = "Get All tempSensorTypes",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = TempSensorType.class)
            )
    )
    @Retry(maxRetries = 5)
    public Response get() {
        final Long invocationNumber = counter.getAndIncrement();

        maybeFail(String.format("TempSensorTypeResource#get() invocation #%d failed", invocationNumber));

        LOGGER.infof("TempSensorTypeResource#get() invocation #%d returning successfully", invocationNumber);
        return Response.ok(tempSensorTypeService.findAll()).build();

    }



    @GET
    @Path("/{tempSensorTypeId}")
    @APIResponse(
            responseCode = "200",
            description = "Get TempSensorType by tempSensorTypeId",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Manufacturer.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "tempSensorType does not exist for tempSensorTypeId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Timeout(300)
    public Response getById(@Parameter(name = "tempSensorTypeId", required = true) @PathParam("tempSensorTypeId") Integer tempSensorTypeId) {
        long started = System.currentTimeMillis();
        final long invocationNumber = counter.getAndIncrement();

        try {
            randomDelay();
            LOGGER.infof("tempSensorTypeResource#getById() invocation #%d returning successfully", invocationNumber);

        return tempSensorTypeService.findById(tempSensorTypeId)
                .map(tempSensorType -> Response.ok(tempSensorType).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (InterruptedException e) {
            LOGGER.errorf("tempSensorTypeResource#getById() invocation #%d timed out after %d ms",
                    invocationNumber, System.currentTimeMillis() - started);
            return null;
        }
    }

    @POST
    @APIResponse(
            responseCode = "201",
            description = "TempSensorType Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = TempSensorType.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid tempSensorType",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "tempSensorType already exists for tempSensorTypeId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response post(@NotNull @Valid TempSensorType tempSensorType, @Context UriInfo uriInfo) {

        tempSensorTypeService.save(tempSensorType);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(tempSensorType.getTempSensorTypeId())).build();
        return Response.created(uri).entity(tempSensorType).build();
    }

    @PUT
    @Path("/{tempSensorTypeId}")
    @APIResponse(
            responseCode = "204",
            description = "tempSensorType updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = TempSensorType.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid tempSensorType",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "tempSensorType object does not have tempSensorTypeId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable tempSensorTypeId does not match TempSensorType.tempSensorTypeId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No tempSensorType found for tempSensorTypeId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response put(@Parameter(name = "tempSensorTypeId", required = true) @PathParam("tempSensorTypeId") Integer tempSensorTypeId, @NotNull @Valid TempSensorType tempSensorType) {
        if (!Objects.equals(tempSensorTypeId, tempSensorType.getTempSensorTypeId())) {
            throw new ServiceException("Path variable tempSensorTypeId does not match TempSensorType.tempSensorTypeId");
        }
        tempSensorTypeService.update(tempSensorType);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private void maybeFail(String failureLogMessage) {
        if (new Random().nextBoolean()) {
            LOGGER.error(failureLogMessage);
            throw new RuntimeException("Resource failure.");
        }
    }

    private void randomDelay() throws InterruptedException {
        Thread.sleep(new Random().nextInt(500));
    }
}
