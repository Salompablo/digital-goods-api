package com.pablo.digitalstore.digital_store_api.controller;

import com.pablo.digitalstore.digital_store_api.exception.ErrorDetails;
import com.pablo.digitalstore.digital_store_api.model.dto.response.OrderResponse;
import com.pablo.digitalstore.digital_store_api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "Operations related to orders and checkout")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Checkout current user's cart",
            description = "Converts the current user's cart into an order"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot checkout an empty cart or invalid request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
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
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('CHECKOUT_CART')")
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkoutCurrentCart() {
        OrderResponse order = orderService.checkoutCurrentCart();
        return ResponseEntity.ok(order);
    }

    @Operation(
            summary = "Get paginated list of current user's orders",
            description = "Returns a page of orders belonging to the authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('VIEW_ORDERS')")
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNumber,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<OrderResponse> ordersPage = orderService.getOrdersByCurrentUser(pageable);
        return ResponseEntity.ok(ordersPage);
    }

    @Operation(
            summary = "Cancel (clear) the current user's cart",
            description = "Empties the current user's cart, removing all items"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/cart/cancel")
    @PreAuthorize("hasAuthority('CANCEL_CART')")
    public ResponseEntity<Void> cancelCurrentCart() {
        orderService.cancelCurrentCart();
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get current pending order",
            description = "Returns the current pending order for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending order retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "No pending order found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PreAuthorize("hasAuthority('VIEW_ORDERS')")
    @GetMapping("/pending")
    public ResponseEntity<OrderResponse> getPendingOrderForCurrentUser() {
        return ResponseEntity.ok(orderService.getPendingOrderForCurrentUser());
    }
}
