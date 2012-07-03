package com.mufumbo.server.scheduler.hazelcast;

import org.quartz.simpl.RAMJobStore;

public class HazelcastRAMJobStore extends RAMJobStore {
	@Override
	public boolean isClustered() {
		return false; // investigate this method.
	}

	/**
	 * TODO: persist data in hbase, http://blog.codepoly.com/distribute-with-hazelcast-persist-into-hbase
	 */
	@Override
	public boolean supportsPersistence() {
		return false;
	}
}
