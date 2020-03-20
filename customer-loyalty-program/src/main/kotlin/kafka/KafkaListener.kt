package sk.bsmk.clp.kafka

import org.springframework.kafka.annotation.KafkaListener


class KafkaListener() {


    @KafkaListener(topics = ["monetary-transactions"])
    fun consumeTransaction() {

    }


}