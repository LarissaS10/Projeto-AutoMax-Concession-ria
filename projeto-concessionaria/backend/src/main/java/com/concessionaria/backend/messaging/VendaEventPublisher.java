package com.concessionaria.backend.messaging;

import com.concessionaria.backend.config.RabbitMQConfig;
import com.concessionaria.backend.dto.VendaEventoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VendaEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarVendaRealizada(VendaEventoDTO evento) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String json = mapper.writeValueAsString(evento);

            log.info("Publicando evento de venda realizada: vendaId={}", evento.getVendaId());
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_CONCESSIONARIA,
                    RabbitMQConfig.ROUTING_VENDA,
                    json
            );
            log.info("Evento publicado com sucesso na fila: {}", RabbitMQConfig.FILA_VENDA_REALIZADA);
        } catch (Exception e) {
            log.error("Erro ao publicar evento de venda: {}", e.getMessage());
        }
    }
}