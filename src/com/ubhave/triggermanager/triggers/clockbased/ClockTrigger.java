/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.triggermanager.triggers.clockbased;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.triggers.Trigger;

public abstract class ClockTrigger extends Trigger
{
	/*
	 * Abstract Clock Trigger
	 * For any kind of triggers that are based on time.
	 * Note that clock triggers cannot be paused; a call to pause them will kill them.
	 */
	
	protected Timer surveyTimer;

	protected class SurveyNotification extends TimerTask
	{
		@Override
		public void run()
		{
			sendNotification();
		}
	}

	public ClockTrigger(Context context, TriggerReceiver listener, TriggerConfig params) throws TriggerException
	{
		super(context, listener, params);
	}

	@Override
	public void kill() throws TriggerException
	{
		super.kill();
		if (surveyTimer != null)
		{
			surveyTimer.cancel();
			surveyTimer = null;
		}
	}

	@Override
	public void pause() throws TriggerException
	{
		super.pause();
		kill();
	}

	@Override
	public void resume() throws TriggerException
	{
		super.resume();
		if (state != RUNNING)
		{
			initialise();
		}
	}
	
	@Override
	protected void initialise() throws TriggerException
	{
		super.initialise();
		surveyTimer = new Timer();
	}
}
