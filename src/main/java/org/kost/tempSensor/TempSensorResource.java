package org.kost.tempSensor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.kost.exceptions.ServiceException;
import org.kost.tempSensorType.TempSensorType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Objects;

@Path("/tempSensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "tempSensor", description = "TempSensor Operations")
@AllArgsConstructor
@Slf4j
public class TempSensorResource {
    private final TempSensorService tempSensorService;

    @GET
    @APIResponse(
            responseCode = "200",
            description = "Get All tempSensors",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = TempSensor.class)
            )
    )
    public Response getAllTempSensor() {
        return Response.ok(tempSensorService.findAll()).build();

    }



    @GET
    @Path("/{tempSensorId}")
    @APIResponse(
            responseCode = "200",
            description = "Get TempSensor by tempSensorId",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = TempSensor.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "tempSensor does not exist for tempSensorId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getByIdTempSensor(@Parameter(name = "tempSensorId", required = true) @PathParam("tempSensorId") Integer tempSensorId) {
        return tempSensorService.findById(tempSensorId)
                .map(tempSensor -> Response.ok(tempSensor).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @APIResponse(
            responseCode = "201",
            description = "TempSensor Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = TempSensor.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid tempSensor",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "tempSensor already exists for tempSensorId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response postCreateNewTempSensor(@NotNull @Valid TempSensor tempSensor, @Context UriInfo uriInfo) {

        tempSensorService.save(tempSensor);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(tempSensor.getTempSensorId())).build();
        return Response.created(uri).entity(tempSensor).build();
    }

    @PUT
    @Path("/{tempSensorId}")
    @APIResponse(
            responseCode = "204",
            description = "tempSensor updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = TempSensor.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid tempSensor",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "tempSensorType object does not have tempSensorId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable tempSensorTypeId does not match TempSensor.tempSensorId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No tempSensorType found for tempSensorId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response putUpdateTempSensor(@Parameter(name = "tempSensorId", required = true) @PathParam("tempSensorId") Integer tempSensorId, @NotNull @Valid TempSensor tempSensor) {
        if (!Objects.equals(tempSensorId, tempSensor.getTempSensorId())) {
            throw new ServiceException("Path variable tempSensorId does not match TempSensor.tempSensorId");
        }
        tempSensorService.update(tempSensor);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @DELETE
    @Path("/{tempSensorId}")
    @APIResponse(
            responseCode = "204",
            description = "TempSensor deleted",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = TempSensor.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid TempSensor",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No TempSensor found for tempSensorId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response deleteTempSensor(Integer tempSensorId){

        tempSensorService.delete(tempSensorId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
