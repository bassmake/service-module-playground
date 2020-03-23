package sk.bsmk.clp.grpc

import io.grpc.ServerBuilder
import mu.KotlinLogging
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@Component
class ClpGrpcServer(
    val service: ClpGrpcService
) {

    private val server = ServerBuilder.forPort(8991).addService(service).build()

    @PostConstruct
    fun start() {
        logger.info { "gRPC server starting" }
        server.start()
        logger.info { "gRPC server started" }
    }

}