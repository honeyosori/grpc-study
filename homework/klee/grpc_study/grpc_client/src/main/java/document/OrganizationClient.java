package document;

import document.global.security.TokenCallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OrganizationClient {
    private static final Logger logger = Logger.getLogger(OrganizationClient.class.getName());

    public static void main(String[] args) {
        TokenCallCredentials callCredentials = new TokenCallCredentials("some-secret-token");

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        DocumentManagementGrpc.DocumentManagementBlockingStub stub = DocumentManagementGrpc.newBlockingStub(channel).withCallCredentials(callCredentials);

        // add Organization
        addOrganization(stub);

        // delete Organization
        deleteOrganization(stub);
    }

    private static void addOrganization(DocumentManagementGrpc.DocumentManagementBlockingStub stub) {
        DocumentManagementOuterClass.Organization organization = DocumentManagementOuterClass.Organization
                .newBuilder()
                .setId(1)
                .setName("sosok1")
                .build();

        DocumentManagementOuterClass.DocumentStorage storage = stub.addOrganization(organization);
        logger.info("AddOrganization Response -> : " + storage.getId() + ", " + storage.getName());
    }

    private static void deleteOrganization(DocumentManagementGrpc.DocumentManagementBlockingStub stub) {
        DocumentManagementOuterClass.OrganizationId organizationId = DocumentManagementOuterClass.OrganizationId
                .newBuilder()
                .setId(2)
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
}
