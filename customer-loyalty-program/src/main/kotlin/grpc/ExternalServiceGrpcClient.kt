package sk.bsmk.clp.grpc

import io.grpc.ManagedChannelBuilder
import mu.KotlinLogging
import org.springframework.stereotype.Service
import sk.bsmk.clp.domain.CustomerId
import sk.bsmk.clp.external.CustomerRegistrationRequest
import sk.bsmk.clp.external.ExternalServiceGrpc

private val logger = KotlinLogging.logger {}

@Service
class ExternalServiceGrpcClient {

    private val target = "localhost:8990"

    private val channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build()
    private val blockingStub = ExternalServiceGrpc.newBlockingStub(channel)
    private val asyncStub = ExternalServiceGrpc.newStub(channel)

    fun sendRegistration(customerId: CustomerId, name: String) {

        val request = CustomerRegistrationRequest.newBuilder().setId(customerId.id.toString()).setName(name).build()

        logger.info { "sending $request via grpc to $target" }
        val reply = blockingStub.registerCustomer(request)
        logger.info { "received reply: $reply" }
        logger.info { "received code: ${reply.code}" }
        logger.info { "received message: ${reply.message}" }
    }

}