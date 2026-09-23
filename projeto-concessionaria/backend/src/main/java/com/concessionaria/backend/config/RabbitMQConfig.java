package com.concessionaria.backend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILA_VENDA_REALIZADA = "venda.realizada";
    public static final String FILA_TESTDRIVE_AGENDADO = "testdrive.agendado";
    public static final String EXCHANGE_CONCESSIONARIA = "concessionaria.exchange";
    public static final String ROUTING_VENDA = "venda.realizada";
    public static final String ROUTING_TESTDRIVE = "testdrive.agendado";

    @Bean
    public Queue filaVendaRealizada() {
        return new Queue(FILA_VENDA_REALIZADA, true);
    }

    @Bean
    public Queue filaTestDriveAgendado() {
        return new Queue(FILA_TESTDRIVE_AGENDADO, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_CONCESSIONARIA);
    }

    @Bean
    public Binding bindingVenda(Queue filaVendaRealizada, TopicExchange exchange) {
        return BindingBuilder.bind(filaVendaRealizada)
                .to(exchange)
                .with(ROUTING_VENDA);
    }

    @Bean
    public Binding bindingTestDrive(Queue filaTestDriveAgendado, TopicExchange exchange) {
        return BindingBuilder.bind(filaTestDriveAgendado)
                .to(exchange)
                .with(ROUTING_TESTDRIVE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}