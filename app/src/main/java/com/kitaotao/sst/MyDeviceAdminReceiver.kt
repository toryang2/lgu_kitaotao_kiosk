package com.kitaotao.sst

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyDeviceAdminReceiver : DeviceAdminReceiver() {

    // This method is called when the device admin is enabled
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        // Handle admin enabled event
        Toast.makeText(context, "Device Admin Enabled", Toast.LENGTH_SHORT).show()
    }

    // This method is called when the device admin is disabled
    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        // Handle admin disabled event
        Toast.makeText(context, "Device Admin Disabled", Toast.LENGTH_SHORT).show()
    }

    // Optionally, you can override other methods like onPasswordChanged, etc.
}
