package com.exactpro.th2.act.events;

import com.exactpro.th2.act.events.verification.VerificationDetail;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.common.schema.factory.CommonFactory;

import java.util.List;
import java.util.Map;

public class StubEventStoreHandler extends EventStoreHandler {
	public StubEventStoreHandler(CommonFactory commonFactory) {
		super(null);
	}

	@Override
	public void storeEvent(EventDetails.EventInfo info, Map<String, String> requestParams, RhBatchResponse response) {
	}

	@Override
	public void storeEvent(EventDetails.EventInfo info, Map<String, String> requestParams) {
	}

	@Override
	public void storeErrorEvent(EventDetails.EventInfo info, Map<String, String> requestParams, String text, Throwable t) {
	}

	@Override
	public void storeVerification(EventDetails.EventInfo info, List<VerificationDetail> details) {
	}
}
