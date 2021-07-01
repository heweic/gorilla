package org.myframe.gorilla.transport;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.extension.SpiMeta;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpiMeta(name = GorillaConstants.DEFAULT_VALUE)
public class HearbeatEndpointFactory extends AbstractEndpointFactory {

	private ScheduledExecutorService executorService = null;

	public HearbeatEndpointFactory() {

		executorService = Executors.newScheduledThreadPool(1);

		executorService.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				Iterator<Entry<Client, HeartbeatFactory>> its = endpoints.entrySet().iterator();

				while (its.hasNext()) {
					Entry<Client, HeartbeatFactory> entry = its.next();
					entry.getKey().heartbeat(entry.getValue().createRequest());
				}

			}
		}, GorillaConstants.HEARTBEAT_PERIOD, GorillaConstants.HEARTBEAT_PERIOD, TimeUnit.MILLISECONDS);
	}
}
