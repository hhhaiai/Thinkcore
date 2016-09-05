package com.thinkcore;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import java.util.UUID;

import com.thinkcore.storage.TStorageUtils;
import com.thinkcore.utils.TStringUtils;
import com.thinkcore.utils.config.TPreferenceConfig;

public class TDeviceUuid {
	protected static final String PREFS_FILE = "device_id.xml";
	protected static final String PREFS_DEVICE_ID = "device_id";
	protected static UUID uuid;

	public TDeviceUuid() {
		if (uuid == null) {
			Class var1 = TDeviceUuid.class;
			synchronized (TDeviceUuid.class) {
				if (uuid == null) {
					String id = TPreferenceConfig.getInstance().getString("device_id", (String) null);
					if (id != null) {
						uuid = UUID.fromString(id);
					} else {
						try {
							id = this.getUuidByPhone();
							if(TStringUtils.isEmpty(id))
								id = this.getMacByPhone();
							if(TStringUtils.isEmpty(id))
								throw new Exception("");
							uuid = UUID.nameUUIDFromBytes(id.getBytes("utf8"));
							TPreferenceConfig.getInstance().setString("device_id", uuid.toString());
						} catch (Exception var5) {
							var5.printStackTrace();
						}
					}
				}
			}
		}

	}


	private String getUuidByPhone() {
		TelephonyManager telephonyManager = (TelephonyManager) TApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		try {
			String result = TStorageUtils.getDeviceId(TApplication.getInstance());
			if (!TStringUtils.isEmpty(result)) {
				return result;
			}
		} catch (Exception e) {
		}

		try {
			String result = telephonyManager.getSimSerialNumber();
			if (!TStringUtils.isEmpty(result)) {
				return result;
			}
		} catch (Exception e) {
		}

		try {
			String result = Secure.getString(TApplication.getInstance().getContentResolver(), "android_id");
			if (!TStringUtils.isEmpty(result)) {
				return result;
			}
		} catch (Exception e) {
		}

		return "";
	}

	private String getMacByPhone() {
		String result = "";
		try {
			WifiManager wm = (WifiManager) TApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
			result = wm.getConnectionInfo().getMacAddress();
		} catch (Exception e) {
		}

		return result;
	}

	public UUID getDeviceUuid() {
		return uuid;
	}
}
