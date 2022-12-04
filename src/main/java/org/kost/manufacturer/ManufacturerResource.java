package org.kost.manufacturer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.kost.exceptions.ServiceException;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.inject.Inject;
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

@Path("/manufacturers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "manufacturer", description = "Manufacturer Operations")
@AllArgsConstructor
@Slf4j

public class ManufacturerResource {


    private AtomicLong counter = new AtomicLong(0);


    private final ManufacturerService manufacturerService;

    private static final Logger LOGGER = Logger.getLogger(ManufacturerService.class);

    @GET
    @APIResponse(
            responseCode = "200",
            description = "Get All Manufacturers",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Manufacturer.class)
            )
    )
    @Timed(name = "manufacturersGetAllTimer", description = "A measure of how long it takes to perform the get of all entities.", unit = MetricUnits.MILLISECONDS)
    @Counted(name = "performedGets", description = "How many all manufacturer gets have been performed.")
    public Response get() {
        final Long invocationNumber = counter.getAndIncrement();

        maybeFail(String.format("ManufacturerService#findAll() invocation #%d failed", invocationNumber));

        LOGGER.infof("ManufacturerService#findAll() invocation #%d returning successfully", invocationNumber);

        return Response.ok(manufacturerService.findAll()).build();

    }

    @GET
    @Path("/{manufacturerId}")
    @APIResponse(
            responseCode = "200",
            description = "Get Manufacturer by manufacturerId",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Manufacturer.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Manufacturer does not exist for manufacturerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Timed(name = "manufacturersGetIdTimer", description = "A measure of how long it takes to perform the get entity by id.", unit = MetricUnits.MILLISECONDS)
    @Counted(name = "performedGetsId", description = "How many manufacturer gets by id have been performed.")
    public Response getById(@Parameter(name = "manufacturerId", required = true) @PathParam("manufacturerId") Integer manufacturerId) {
        return manufacturerService.findById(manufacturerId)
                .map(manufacturer -> Response.ok(manufacturer).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @APIResponse(
            responseCode = "201",
            description = "Manufacturer Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Manufacturer.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Manufacturer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Manufacturer already exists for manufacturerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response post(@NotNull @Valid Manufacturer manufacturer, @Context UriInfo uriInfo) {

        manufacturerService.save(manufacturer);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(manufacturer.getManufacturerId())).build();
        return Response.created(uri).entity(manufacturer).build();
    }

    @PUT
    @Path("/{manufacturerId}")
    @APIResponse(
            responseCode = "204",
            description = "Manufacturer updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Manufacturer.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Manufacturer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Manufacturer object does not have manufacturerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable manufacturerId does not match Manufacturer.manufacturerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Manufacturer found for manufacturerId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response put(@Parameter(name = "manufacturerId", required = true) @PathParam("manufacturerId") Integer manufacturerId, @NotNull @Valid Manufacturer manufacturer) {
        if (!Objects.equals(manufacturerId, manufacturer.getManufacturerId())) {
            throw new ServiceException("Path variable manufacturerId does not match Manufacturer.manufacturerId");
        }
        manufacturerService.update(manufacturer);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @DELETE
    @Path("/{manufacturerId}")
    @APIResponse(
            responseCode = "204",
            description = "Manufacturer deleted",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = Manufacturer.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid Manufacturer",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Manufacturer object does not have manufacturerId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No Manufacturer found for manufacturerId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response delete(Integer manufacturerId){

        manufacturerService.delete(manufacturerId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private void maybeFail(String failureLogMessage) {
        if (new Random().nextBoolean()) {
            LOGGER.error(failureLogMessage);
            throw new RuntimeException("Resource failure.");
        }
    }
}
