package net.javaguides.orderservice.publisher;

import net.javaguides.orderservice.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private Logger logger = LoggerFactory.getLogger(OrderProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.routing.key}")
    private String orderRoutingKey;

    @Value("${rabbitmq.binding.email.routing.key}")
    private String emailRoutingKey;

    private RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(OrderEvent orderEvent) {
        logger.info(String.format("Oreder event sent to RabbitMQ -> %s", orderEvent.toString()));

        // send the order event to the order queue
        rabbitTemplate.convertAndSend(exchange, orderRoutingKey, orderEvent);

        // send the order event to the email queue
        rabbitTemplate.convertAndSend(exchange, emailRoutingKey, orderEvent);
    }
}
