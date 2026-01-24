package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//make sure to extend ImplBase class from protobuf
@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest billingRequest, StreamObserver<BillingResponse> responseObserver) {
        log.info("createBillingAccount request received {}",billingRequest.toString());

        //business logic e.g save to db

        BillingResponse response = BillingResponse.newBuilder().setAccountId("12345").setStatus("Active").build();
        responseObserver.onNext(response); //you can have more than 1 onNext, used to send responses to client
        responseObserver.onCompleted();//end the cycle

    }
}
