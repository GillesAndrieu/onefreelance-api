package org.grisbi.onefreelance.api.swagger.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.grisbi.onefreelance.model.errors.ApiError;

/**
 * Swagger : DeleteClientDocumentation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Delete client",
    description = "Delete client information.",
    security = {@SecurityRequirement(name = "jwt")},
    responses = {
        @ApiResponse(
            responseCode = "204",
            description = "The client deleted"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "The provided token is not valid",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = ApiError.class))),
    })
public @interface DeleteClientDocumentation {
}

