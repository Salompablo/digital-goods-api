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
            description = "Returns the current authenticated user's cart with all items."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<CartResponse> getCurrentCart() {
        return ResponseEntity.ok(cartService.getCurrentCart());
    }

    @Operation(
            summary = "Add product to current user's cart",
            description = "Adds a product to the authenticated user's cart, or updates the quantity if already present."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added/updated successfully",
                    content = @Content(schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PostMapping("/me/items")
    public ResponseEntity<CartResponse> addProductToCart(
            @Parameter(description = "Product and quantity to add", required = true)
            @Valid @RequestBody AddProductToCartRequest request) {

        return ResponseEntity.ok(cartService.addProductToCurrentCart(request));
    }

    @Operation(
            summary = "Decrease product quantity in current user's cart",
            description = "Decreases the quantity of a product in the authenticated user's cart. Removes the product if quantity reaches zero."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product quantity decreased or item removed",
                    content = @Content(schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not in cart or cart not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PostMapping("/me/items/{productId}/decrease")
    public ResponseEntity<CartResponse> decreaseProductQuantity(
            @Parameter(description = "Product ID to decrease", required = true)
            @PathVariable Long productId) {

        return ResponseEntity.ok(cartService.decreaseProductQuantityInCurrentCart(productId));
    }

    @Operation(
            summary = "Remove product from current user's cart",
            description = "Removes a specific product from the authenticated user's cart, regardless of quantity."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed from cart",
                    content = @Content(schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not in cart or cart not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @DeleteMapping("/me/items/{productId}")
    public ResponseEntity<CartResponse> removeProductFromCart(
            @Parameter(description = "Product ID to remove", required = true)
            @PathVariable Long productId) {

        return ResponseEntity.ok(cartService.removeProductFromCurrentCart(productId));
    }

    @Operation(
            summary = "Clear current user's cart",
            description = "Removes all products from the authenticated user's cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully",
                    content = @Content(schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    @DeleteMapping("/me/clear")
    public ResponseEntity<CartResponse> clearCart() {
        return ResponseEntity.ok(cartService.clearCurrentCart());
    }
}