package com.prgrms.rg.infrastructure.oauth;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

public interface Communicator {
	ConcurrentMap<String, String> convertAuthorizationCodeToInfo(String authorizationCode) throws IOException, InterruptedException;
}
