package org.grisbi.onefreelance.api.swagger.contract;

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
 * Swagger : DeleteContractDocumentation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Delete contract",
    description = "Delete contract information.",
    security = {@SecurityRequirement(name = "jwt")},
    responses = {
        @ApiResponse(
            responseCode = "204",
            description = "The contract deleted"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "The provided token is not valid",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = ApiError.class))),
    })
public @interface DeleteContractDocumentation {
}

