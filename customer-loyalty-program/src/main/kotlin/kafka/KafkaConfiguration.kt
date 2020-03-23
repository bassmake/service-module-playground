package sk.bsmk.clp.kafka

import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.support.serializer.JsonDeserializer
import sk.bsmk.clp.shared.MonetaryTransactionDto


@Configuration
@EnableKafka
class KafkaConfiguration {

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: DefaultKafkaConsumerFactory<String, MonetaryTransactionDto>):
            KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MonetaryTransactionDto>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, MonetaryTransactionDto>()
        factory.consumerFactory = consumerFactory
        factory.setConcurrency(3)
        factory.containerProperties.pollTimeout = 3000
        factory.setMissingTopicsFatal(false)
        return factory
    }

    @Bean
    fun consumerFactory(properties: KafkaProperties): DefaultKafkaConsumerFactory<String, MonetaryTransactionDto> {
        val props = properties.buildConsumerProperties()
        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            JsonDeserializer(MonetaryTransactionDto::class.java)
                .ignoreTypeHeaders()
        )
    }

}