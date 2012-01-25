package org.sipdroid.media;

import java.util.Set;

import org.sipdroid.sipua.ui.Receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothClass.Device;
import android.bluetooth.BluetoothClass.Service;
import android.content.Context;
import android.media.AudioManager;
import android.os.Looper;

/*
 * Copyright (C) 2010 The Sipdroid Open Source Project
 * Copyright (C) 2007 The Android Open Source Project
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

public class Bluetooth {

	static BluetoothAdapter ba;
	static AudioManager am;
	
	public static void init() {
		if (ba == null) {
			
			//http://code.google.com/p/android/issues/detail?id=16587
			//
			//found some similar solutions here:
			// http://code.google.com/p/android-scripting/source/browse/android/BluetoothFacade/src/com/google/ase/facade/BluetoothFacade.java?r=9407d0bd872fb5375f813fca17c8ef2207b5256b
			// here:      http://android-wifi-tether.googlecode.com/svn-history/r324/trunk/src/android/tether/TetherApplication.java
			// and here:  http://stackoverflow.com/questions/6856733/bluetooth-connection-timed-out-too-soon	
			//all three links call Looper.prepare() before get the bluetooth adapter
			Looper.prepare();
			
			
			ba = BluetoothAdapter.getDefaultAdapter();
			am = (AudioManager) Receiver.mContext.getSystemService(
	                Context.AUDIO_SERVICE);
		}
	}
	
	public static void enable(boolean mode) {
		if (mode)
			am.startBluetoothSco();
		else
			am.stopBluetoothSco();
	}
	
	public static boolean isAvailable() {
		if (!ba.isEnabled())
			return false;
		Set<BluetoothDevice> devs = ba.getBondedDevices();
		for (final BluetoothDevice dev : devs) {
			BluetoothClass cl = dev.getBluetoothClass();
			if (cl != null && (cl.hasService(Service.RENDER) ||
					cl.getDeviceClass() == Device.AUDIO_VIDEO_HANDSFREE ||
					cl.getDeviceClass() == Device.AUDIO_VIDEO_CAR_AUDIO ||
					cl.getDeviceClass() == Device.AUDIO_VIDEO_WEARABLE_HEADSET))
				return true;
		}
		return false;
	}
	
	public static boolean isSupported() {
		init();
		return am.isBluetoothScoAvailableOffCall();
	}
}
