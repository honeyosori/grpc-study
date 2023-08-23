package document;

import document.global.security.TokenCallCredentials;
import document.organization.OrganizationClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentManagementClient {
    private static final Logger logger = Logger.getLogger(DocumentManagementClient.class.getName());

    public static void main(String[] args) {
        OrganizationClient organizationClient = new OrganizationClient("localhost", 50051);

        // add Organization
        organizationClient.addOrganization(1, "sosok1");

        // delete Organization
        organizationClient.deleteOrganization(1);
    }
}
