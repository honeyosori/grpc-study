package document;

import document.organization.OrganizationClient;
import io.grpc.ManagedChannel;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DocumentManagementClientTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Mock
    private ManagedChannel mockChannel;

    @Mock
    private DocumentManagementGrpc.DocumentManagementBlockingStub mockStub;

    private OrganizationClient client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockChannel.shutdownNow()).thenReturn(mockChannel);
        when(mockChannel.shutdown()).thenReturn(mockChannel);
        when(mockChannel.isShutdown()).thenReturn(false);

        client = new OrganizationClient(mockChannel, mockStub);
    }

    @Test
    @DisplayName("소속 생성 시 문서함 생성")
    void testAddOrganization() {
        DocumentManagementOuterClass.DocumentStorage response = DocumentManagementOuterClass.DocumentStorage.newBuilder()
                .setId(1)
                .setName("sosok1_storage")
                .build();

        when(mockStub.addOrganization(any())).thenReturn(response);

        String storageName = client.addOrganization(1, "sosok1");

        Assertions.assertEquals("sosok1_storage", storageName);
    }

    @AfterEach
    void tearDown() {
        client.shutdown();
    }
}
