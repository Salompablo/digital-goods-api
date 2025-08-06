package com.pablo.digitalstore.digital_store_api.controller;

import com.pablo.digitalstore.digital_store_api.exception.ErrorDetails;
import com.pablo.digitalstore.digital_store_api.model.dto.request.AddProductToCartRequest;
import com.pablo.digitalstore.digital_store_api.model.dto.response.CartResponse;
import com.pablo.digitalstore.digital_store_api.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "Operations related to the shopping cart")
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Get current user's cart",
            description = "Returns the cart of the authenticated user, including all items."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cart retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CartResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('VIEW_ORDERS')")
    @GetMapping("/me")
    public ResponseEntity<CartResponse> getCurrentCart() {
        return ResponseEntity.ok(cartService.getCurrentCart());
    }

    @Operation(
            summary = "Add product to cart",
            description = "Adds a product to the current user's cart or increases quantity if it already exists."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product added/updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CartResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('UPDATE_CART')")
    @PostMapping("/me/items")
    public ResponseEntity<CartResponse> addProductToCart(
            @Parameter(description = "Product and quantity to add", required = true)
            @Valid @RequestBody AddProductToCartRequest request) {
        return ResponseEntity.ok(cartService.addProductToCurrentCart(request));
    }

    @Operation(
            summary = "Decrease product quantity in cart",
            description = "Decreases the quantity of a product in the current user's cart or removes it if it reaches zero."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quantity decreased or item removed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CartResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not in cart or cart not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('UPDATE_CART')")
    @PostMapping("/me/items/{productId}/decrease")
    public ResponseEntity<CartResponse> decreaseProductQuantity(
            @Parameter(description = "ID of the product to decrease", example = "1")
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.decreaseProductQuantityInCurrentCart(productId));
    }

    @Operation(
            summary = "Remove product from cart",
            description = "Removes a product from the current user's cart, regardless of quantity."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product removed from cart",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CartResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not in cart or cart not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('UPDATE_CART')")
    @DeleteMapping("/me/items/{productId}")
    public ResponseEntity<CartResponse> removeProductFromCart(
            @Parameter(description = "ID of the product to remove", example = "1")
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCurrentCart(productId));
    }

    @Operation(
            summary = "Clear cart",
            description = "Clears all items from the current user's cart."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cart cleared successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CartResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cart not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('CANCEL_CART')")
    @DeleteMapping("/me/clear")
    public ResponseEntity<CartResponse> clearCart() {
        return ResponseEntity.ok(cartService.clearCurrentCart());
    }
}