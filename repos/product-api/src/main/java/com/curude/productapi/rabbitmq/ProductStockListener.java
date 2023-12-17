package com.curude.productapi.rabbitmq;

import com.curude.productapi.dto.ProductStockDTO;
import com.curude.productapi.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductStockListener {

    @Autowired
    private ProductService service;

    @RabbitListener(queues = "{app-config.rabbit.queue.product-stock}")
    public void receiveProductStockMessage(ProductStockDTO productDTO) throws JsonProcessingException {
        log.info("recebendo msg: {}", new ObjectMapper().writeValueAsString(productDTO));
        service.updateProductStock(productDTO);

    }

}
