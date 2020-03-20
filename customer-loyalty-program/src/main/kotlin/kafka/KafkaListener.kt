package sk.bsmk.clp.kafka

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import sk.bsmk.clp.shared.MonetaryTransactionDto
import sk.bsmk.clp.usecases.MonetaryTransactionProcessingUseCase

private val logger = KotlinLogging.logger {}

@Component
class KafkaListener(val processing: MonetaryTransactionProcessingUseCase) {

    @KafkaListener(topics = ["monetary-transactions"])
    fun consumeTransaction(record: ConsumerRecord<String, MonetaryTransactionDto>) {
        logger.debug { "Received $record" }
        processing.process(record.value())
    }

}