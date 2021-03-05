package com.ssh.shuoshi.library.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
	private ThreadManager() {

	}

	private static ThreadManager instance = new ThreadManager();
	private ThreadPoolProxy longPool;
	private ThreadPoolProxy shortPool;

	public static ThreadManager getInstance() {
		return instance;
	}

	public synchronized ThreadPoolProxy createLongPool() {
		if (longPool == null) {
			longPool = new ThreadPoolProxy(5, 5, 5000L);
		}
		return longPool;
	}
	
	public synchronized ThreadPoolProxy createShortPool() {
		if(shortPool==null){
			shortPool = new ThreadPoolProxy(3, 3, 5000L);
		}
		return shortPool;
	}

	public class ThreadPoolProxy {
		private ThreadPoolExecutor pool;
		private int corePoolSize;
		private int maximumPoolSize;
		private long time;

		public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long time) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.time = time;

		}
		
		public void execute(Runnable runnable) {
			if (pool == null) {
				pool = new ThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE,
						time, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(20));
//				pool.allowsCoreThreadTimeOut();
			}
			if(!pool.isShutdown()){
				pool.execute(runnable); 
			}
		}
		
		public void cancel(Runnable runnable) {
			if (pool != null && !pool.isShutdown() && !pool.isTerminated()) {
				pool.remove(runnable);
			}
		}
	}
}
