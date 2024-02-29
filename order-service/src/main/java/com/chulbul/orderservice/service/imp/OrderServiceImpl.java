package com.chulbul.orderservice.service.imp;

import com.chulbul.orderservice.dto.InventoryResponse;
import com.chulbul.orderservice.dto.OrderLineItemsDto;
import com.chulbul.orderservice.dto.OrderRequest;
import com.chulbul.orderservice.entity.Order;
import com.chulbul.orderservice.entity.OrderLineItems;
import com.chulbul.orderservice.repository.OrderRepo;
import com.chulbul.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    WebClient.Builder webClientBuilder;
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto).toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

//        System.out.println(inventoryResponsesArray[0].isInStock());

        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);
        if (allProductsInStock)
        orderRepo.save(order);
        else throw new IllegalArgumentException("Product out of stock");
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
         OrderLineItems orderLineItems = new OrderLineItems();
         orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
         orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
         orderLineItems.setPrice(orderLineItemsDto.getPrice());
         return orderLineItems;
    }
}
