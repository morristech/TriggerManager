package com.ubhave.triggermanager.triggers.sensorbased;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.triggermanager.TriggerException;
import com.ubhave.triggermanager.TriggerReceiver;
import com.ubhave.triggermanager.config.Constants;
import com.ubhave.triggermanager.triggers.Trigger;

public abstract class SensorTrigger extends Trigger implements SensorDataListener
{
	private final static String LOG_TAG = "SensorTrigger";
	protected final ESSensorManagerInterface sensorManager;
	private final int sensorType, subscriptionId;

	public SensorTrigger(Context context, TriggerReceiver listener, int sensorType) throws TriggerException, ESException
	{
		super(context, listener);
		sensorManager = ESSensorManager.getSensorManager(context);
		this.sensorType = sensorType;
		subscriptionId = registerWithManager();
	}

	private int registerWithManager() throws ESException
	{
		if (Constants.TEST_MODE)
		{
			ESLogger.log(LOG_TAG, "Registering with sensor: " + sensorType);
		}
		return sensorManager.subscribeToSensorData(sensorType, this);
	}

	@Override
	public void kill()
	{
		try
		{
			sensorManager.unsubscribeFromSensorData(subscriptionId);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		listener.onCrossingLowBatteryThreshold(isBelowThreshold);
	}

	@Override
	public void pause()
	{
		try
		{
			sensorManager.pauseSubscription(subscriptionId);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void resume()
	{
		try
		{
			sensorManager.unPauseSubscription(subscriptionId);
		}
		catch (ESException e)
		{
			e.printStackTrace();
		}
	}
}