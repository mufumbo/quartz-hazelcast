package com.mufumbo.server.scheduler.hazelcast;

import java.util.concurrent.locks.Lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import org.springframework.scheduling.SchedulingException;

import com.hazelcast.core.Hazelcast;

public class SchedulerFactoryBean extends org.springframework.scheduling.quartz.SchedulerFactoryBean {
	Log log = LogFactory.getLog(this.getClass());

	Lock lock;

	protected String lockName;

	public SchedulerFactoryBean() {
		super();
		this.setStartupDelay(60);
		this.setAutoStartup(true);
		log.info("created");
	}

	public void setLockName(final String lockName) {
		this.lockName = lockName;
	}

	public Lock getLock() {
		if (lock == null) {
			lock = Hazelcast.getLock(lockName != null ? lockName : "defaultQuartzLock");
		}
		return lock;
	}

	@Override
	public void setStartupDelay(final int startupDelay) {
		// minimum 60 seconds delay
		if (startupDelay < 60) {
			log.warn("trying to setup a startup delay of " + startupDelay);
		}
		else {
			super.setStartupDelay(startupDelay);
		}
	}

	@Override
	public void start() throws SchedulingException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Lock lock = getLock();
				lock.lock();
				log.warn("This node is the master Quartz");
				SchedulerFactoryBean.super.start();
			}
		}).start();
		log.info("Starting..");
	}

	@Override
	public void destroy() throws SchedulerException {
		super.destroy();
		getLock().unlock();
	}
}