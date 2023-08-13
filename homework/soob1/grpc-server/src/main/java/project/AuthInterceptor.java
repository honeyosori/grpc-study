package project;

import io.grpc.*;

public class AuthInterceptor implements ServerInterceptor {

	@Override
	public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
		Metadata.Key<String> authorizationKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
		String authorizationHeader = headers.get(authorizationKey);

		if (authorizationHeader == null) {
			call.close(Status.UNAUTHENTICATED.withDescription("Authorization header is null"), new Metadata());
		}

		return next.startCall(call, headers);
	}
}
