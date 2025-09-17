package cn.karsonluos.aos.common.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import cn.karsonluos.aos.common.components.KsActivityLifecycleCallbacks

object KsArgs {
    fun init(application: Application)
    {
        application.registerActivityLifecycleCallbacks(object : KsActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                val bundle = savedInstanceState ?: activity.intent.extras
                bundle?.let { activity.argRegistry().inject(it) }

                if (activity is FragmentActivity){
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(
                            fm: FragmentManager,
                            f: Fragment,
                            savedInstanceState: Bundle?
                        ) {
                            val bundle = savedInstanceState ?: f.arguments ?: return
                            f.argRegistry().inject(bundle)
                        }

                        override fun onFragmentSaveInstanceState(
                            fm: FragmentManager,
                            f: Fragment,
                            outState: Bundle
                        ) {
                            f.argRegistry().save(outState)
                        }
                    }, true)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                activity.argRegistry().save(outState)
            }
        })
    }
}
