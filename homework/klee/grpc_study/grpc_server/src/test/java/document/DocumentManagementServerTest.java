package document;

import document.module.service.DocumentServiceImpl;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DocumentManagementServerTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Test
    @DisplayName("소속 생성 시 문서함 생성")
    void testAddOrganization() throws Exception {
        // Start an in-process gRPC server
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder.forName(serverName)
                .addService(new DocumentServiceImpl())
                .directExecutor()
                .build()
                .start());

        // Create a client channel
        ManagedChannel channel = grpcCleanup.register(InProcessChannelBuilder.forName(serverName)
                .directExecutor()
                .build());

        // Create a client stub
        DocumentManagementGrpc.DocumentManagementBlockingStub blockingStub = DocumentManagementGrpc.newBlockingStub(channel);

        // Make a request and verify the response
        DocumentManagementOuterClass.Organization request = DocumentManagementOuterClass.Organization
                .newBuilder()
                .setId(1)
                .setName("sosok1")
                .build();

        DocumentManagementOuterClass.DocumentStorage response = blockingStub.addOrganization(request);
        System.out.println("storage name = " + response.getName());
        Assertions.assertEquals("sosok1_storage", response.getName());
    }
}
