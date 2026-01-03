package collectorEvent.gRPC;

import collectorEvent.gRPC.telemetry.event.CollectorControllerGrpc;
import collectorEvent.gRPC.telemetry.event.HubEventProto;
import collectorEvent.gRPC.telemetry.event.SensorEventProto;
import collectorEvent.service.ServiceEvent;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class GRPCController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final ServiceEvent serviceEvent;

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Запрос SensorEventProto: {}", request);
        try {
            serviceEvent.processSensorEvent(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError(responseObserver, e, "collectSensorEvent");
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Запрос HubEventProto: {}", request);
        try {
            serviceEvent.processHubEvent(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError(responseObserver, e, "collectHubEvent");
        }
    }

    private void handleError(StreamObserver<?> responseObserver, Exception e, String context) {
        log.error("Ошибка в {}: {}", context, e.getMessage(), e);
        responseObserver.onError(new StatusRuntimeException(
                Status.INTERNAL
                        .withDescription(e.getLocalizedMessage())
                        .withCause(e)
        ));
    }
}