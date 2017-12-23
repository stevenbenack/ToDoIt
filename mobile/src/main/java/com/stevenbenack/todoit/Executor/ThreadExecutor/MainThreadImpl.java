package com.stevenbenack.todoit.Executor.ThreadExecutor;

import android.os.Handler;
import android.os.Looper;

import com.stevenbenack.todoit.Executor.MainThread;

public class MainThreadImpl implements MainThread{
	private Handler handler;

	MainThreadImpl() {
		this.handler = new Handler(Looper.getMainLooper());
	}

	public void post(Runnable runnable) {
		handler.post(runnable);
	}
}