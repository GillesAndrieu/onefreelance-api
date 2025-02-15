package org.grisbi.onefreelance.api.swagger.customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.grisbi.onefreelance.model.dto.response.CustomerResponse;
import org.grisbi.onefreelance.model.errors.ApiError;

/**
 * Swagger : PostCustomerDocumentation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Post a new customer",
    description = "Create a new customer.",
    security = {@SecurityRequirement(name = "jwt")},
    responses = {
        @ApiResponse(
            responseCode = "201",
            description = "The customer content",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = CustomerResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "The provided body is not correct",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = ApiError.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A customer already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = ApiError.class))),
        @ApiResponse(
            responseCode = "401",
            description = "The provided token is not valid",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = ApiError.class))),
    })
public @interface PostCustomerDocumentation {
}

