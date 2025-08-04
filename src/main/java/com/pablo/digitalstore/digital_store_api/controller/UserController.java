package com.pablo.digitalstore.digital_store_api.controller;

import com.pablo.digitalstore.digital_store_api.exception.ErrorDetails;
import com.pablo.digitalstore.digital_store_api.model.dto.request.ChangePasswordRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.request.UserUpdateRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.ProductResponse;
import com.pablo.digitalstore.digital_store_api.model.dto.response.UserResponse;
import com.pablo.digitalstore.digital_store_api.model.entity.CredentialsEntity;
import com.pablo.digitalstore.digital_store_api.model.mapper.UserMapper;
import com.pablo.digitalstore.digital_store_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "Operations related to user account")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get current authenticated user", description = "Returns the details of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('VIEW_SELF')")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Operation(summary = "Update current user", description = "Updates the profile of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('UPDATE_SELF')")
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(@RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @Operation(summary = "Deactivate current user", description = "Deactivates the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deactivated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('DEACTIVATE_SELF')")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deactivateCurrentUser() {
        userService.deactivateCurrentUser();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change user password", description = "Changes the password of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password or mismatch",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('CHANGE_PASSWORD')")
    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changeMyPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all users", description = "Returns a paginated list of users. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sort) {

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sort));
        return ResponseEntity.ok(userService.findAllUsers(pageable));
    }

    @Operation(summary = "Get user by ID", description = "Returns a user's details by ID. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Operation(summary = "Update user by ID", description = "Updates user details by ID. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUserById(id, request));
    }

    @Operation(summary = "Deactivate user by ID", description = "Deactivates a user by ID. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deactivated"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUserById(@PathVariable Long id) {
        userService.deactivateUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reactivate user by ID", description = "Reactivates a previously deactivated user. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User reactivated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateUserById(@PathVariable Long id) {
        userService.reactivateUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reactivate current user", description = "Reactivates the currently authenticated user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account reactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Account already active",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('REACTIVATE_SELF')")
    @PutMapping("/reactivate")
    public ResponseEntity<Void> reactivateMyAccount() {
        userService.reactivateCurrentUser();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get purchased products", description = "Returns the list of products purchased by the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('VIEW_PURCHASES')")
    @GetMapping("/me/products")
    public ResponseEntity<List<ProductResponse>> getMyPurchasedProducts() {
        return ResponseEntity.ok(userService.getPurchasedProductsForCurrentUser());
    }
}

