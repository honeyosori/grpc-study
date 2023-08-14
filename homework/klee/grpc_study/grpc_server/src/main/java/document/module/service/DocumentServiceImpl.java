package document.module.service;

import document.DocumentManagementGrpc;
import document.DocumentManagementOuterClass;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class DocumentServiceImpl extends DocumentManagementGrpc.DocumentManagementImplBase {
    private Map documentStorageMap = new HashMap<Integer, DocumentManagementOuterClass.DocumentStorage>();

    @Override
    public void addOrganization(
            DocumentManagementOuterClass.Organization request,
            StreamObserver<DocumentManagementOuterClass.DocumentStorage> responseObserver
    ) {
        DocumentManagementOuterClass.DocumentStorage documentStorage = DocumentManagementOuterClass.DocumentStorage.newBuilder()
                .setId(request.getId())
                .setName(request.getName() + "_storage")
                .build();
        documentStorageMap.put(request.getId(), documentStorage);

        responseObserver.onNext(documentStorage);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteOrganization(
            DocumentManagementOuterClass.OrganizationId request,
            StreamObserver<DocumentManagementOuterClass.EmptyMessage> responseObserver
    ) {
        int organizationId = request.getId();
        if(documentStorageMap.containsKey(organizationId)) {
            documentStorageMap.remove(organizationId);
            responseObserver.onNext(DocumentManagementOuterClass.EmptyMessage.newBuilder().build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asException());
        }

    }
}
