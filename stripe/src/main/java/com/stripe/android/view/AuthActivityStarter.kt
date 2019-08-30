package com.stripe.android.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import java.lang.ref.WeakReference

internal interface AuthActivityStarter<StartDataType> {
    fun start(data: StartDataType)

    /**
     * A representation of an object (i.e. Activity or Fragment) that can start an activity.
     */
    class Host private constructor(activity: Activity, fragment: Fragment?) {
        private val activityRef: WeakReference<Activity> = WeakReference(activity)
        private val fragmentRef: WeakReference<Fragment>? =
            if (fragment != null) WeakReference(fragment) else null

        val activity: Activity?
            get() = activityRef.get()

        fun startActivityForResult(target: Class<*>, extras: Bundle, requestCode: Int) {
            val activity = activityRef.get() ?: return

            val intent = Intent(activity, target).putExtras(extras)

            if (fragmentRef != null) {
                val fragment = fragmentRef.get()
                fragment?.startActivityForResult(intent, requestCode)
            } else {
                activity.startActivityForResult(intent, requestCode)
            }
        }

        companion object {
            @JvmStatic
            fun create(fragment: Fragment): Host {
                return Host(fragment.requireActivity(), fragment)
            }

            @JvmStatic
            fun create(activity: Activity): Host {
                return Host(activity, null)
            }
        }
    }
}