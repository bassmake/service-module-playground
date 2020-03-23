package sk.bsmk.clp.grpc

import io.grpc.stub.StreamObserver
import mu.KotlinLogging
import org.springframework.stereotype.Service
import sk.bsmk.clp.usecases.RegistrationUseCase

private val logger = KotlinLogging.logger {}

@Service
class ClpGrpcService(
    val registration: RegistrationUseCase
)  : ClpServiceGrpc.ClpServiceImplBase() {

    override fun register(request: ClpRegistrationRequest, responseObserver: StreamObserver<ClpCustomerGrpcDetail>) {

        logger.info { "registering customer with $request" }

        val entity = registration.register(request.customerName)
        val response = ClpCustomerGrpcDetail.newBuilder()
            .setId(entity.id.str())
            .setName(entity.name)
            .setTier(entity.tier.toString())
            .setPoints(entity.points)
            .build()

        responseObserver.onNext(response)

        logger.info { "registration done, providing response: $response" }

        responseObserver.onCompleted()
    }
}