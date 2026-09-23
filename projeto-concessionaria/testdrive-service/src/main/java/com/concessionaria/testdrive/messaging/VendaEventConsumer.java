package com.concessionaria.testdrive.messaging;

import com.concessionaria.testdrive.dto.VendaEventoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VendaEventConsumer {

    @RabbitListener(queues = "venda.realizada")
    public void consumirVendaRealizada(String mensagem) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

            VendaEventoDTO evento = mapper.readValue(mensagem, VendaEventoDTO.class);

            log.info("Evento recebido — Venda realizada!");
            log.info("Venda ID: {}", evento.getVendaId());
            log.info("Carro: {} {} (ID: {})", evento.getCarroMarca(),
                    evento.getCarroModelo(), evento.getCarroId());
            log.info("Cliente: {} (ID: {})", evento.getClienteNome(),
                    evento.getClienteId());
            log.info("Valor Final: R$ {}", evento.getValorFinal());
            log.info("Data da Venda: {}", evento.getDataVenda());
            log.info("--- Notificacao: O carro {} {} foi vendido para {}. ---",
                    evento.getCarroMarca(), evento.getCarroModelo(),
                    evento.getClienteNome());
        } catch (Exception e) {
            log.error("Erro ao processar evento de venda: {}", e.getMessage());
        }
    }
}