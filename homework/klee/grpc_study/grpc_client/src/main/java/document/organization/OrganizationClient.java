package document.organization;

import document.DocumentManagementClient;
import document.DocumentManagementGrpc;
import document.DocumentManagementOuterClass;
import document.global.security.TokenCallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OrganizationClient {
    private static final Logger logger = Logger.getLogger(OrganizationClient.class.getName());

    private final ManagedChannel channel;
    private final DocumentManagementGrpc.DocumentManagementBlockingStub stub;

    public OrganizationClient(ManagedChannel channel, DocumentManagementGrpc.DocumentManagementBlockingStub stub) {
        this.channel = channel;
        this.stub = stub;
    }

    public OrganizationClient(String host, int port) {
        TokenCallCredentials callCredentials = new TokenCallCredentials("some-secret-token");

        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = DocumentManagementGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);
    }

    public String addOrganization(int id, String name) {
        DocumentManagementOuterClass.Organization organization = DocumentManagementOuterClass.Organization
                .newBuilder()
                .setId(id)
                .setName(name)
                .build();

        DocumentManagementOuterClass.DocumentStorage storage = stub.addOrganization(organization);
        logger.info("AddOrganization Response -> : " + storage.getId() + ", " + storage.getName());
        return storage.getName();
    }

    public void deleteOrganization(int id) {
        DocumentManagementOuterClass.OrganizationId organizationId = DocumentManagementOuterClass.OrganizationId
                .newBuilder()
                .setId(id)
                .build();
        try {
            stub.deleteOrganization(organizationId);
            logger.info("DeleteOrganization Success");
        } catch (io.grpc.StatusRuntimeException e) {
            if(e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                logger.log(Level.WARNING, "storage not found");
            } else {
                logger.log(Level.WARNING, "fail to delete storage");
            }
        }
    }

    public void shutdown() {
        channel.shutdown();
    }
}
