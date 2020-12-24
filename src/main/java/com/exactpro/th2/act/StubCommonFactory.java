package com.exactpro.th2.act;

import com.exactpro.th2.common.grpc.EventBatch;
import com.exactpro.th2.common.schema.factory.CommonFactory;
import com.exactpro.th2.common.schema.message.MessageRouter;

public class StubCommonFactory extends CommonFactory {
	@Override
	public MessageRouter<EventBatch> getEventBatchRouter() {
		return null;
	}
}
