package project;

import io.grpc.CallCredentials;
import io.grpc.Metadata;

import java.util.concurrent.Executor;

public class TokenCallCredentials extends CallCredentials {

	private final String token;

	public TokenCallCredentials(String token) {
		this.token = token;
	}

	@Override
	public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
		appExecutor.execute(() -> {
			Metadata headers = new Metadata();
			Metadata.Key<String> authorizationKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
			headers.put(authorizationKey, "Bearer " + token);
			applier.apply(headers);
		});
	}
}
