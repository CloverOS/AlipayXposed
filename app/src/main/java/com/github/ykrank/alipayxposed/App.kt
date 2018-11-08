package com.github.ykrank.alipayxposed

import android.content.Context
import android.support.multidex.MultiDexApplication
import android.support.v7.preference.PreferenceManager
import com.facebook.stetho.Stetho
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ykrank.alipayxposed.app.data.db.AppDaoOpenHelper
import com.github.ykrank.alipayxposed.app.data.db.AppDaoSessionManager
import com.github.ykrank.alipayxposed.app.data.db.BillDetailsRawDbWrapper
import com.github.ykrank.alipayxposed.app.data.pref.AppPreferences
import com.github.ykrank.alipayxposed.app.data.pref.AppPreferencesImpl
import com.github.ykrank.alipayxposed.app.data.pref.AppPreferencesManager
import com.github.ykrank.androidtools.DefaultAppDataProvider
import com.github.ykrank.androidtools.GlobalData
import com.github.ykrank.androidtools.util.L

class App : MultiDexApplication() {

    val objectMapper = ObjectMapper()

    lateinit var billDb: BillDetailsRawDbWrapper
    lateinit var dbSessionManager:AppDaoSessionManager

    override fun onCreate() {
        super.onCreate()

        app = this

        GlobalData.init(object : DefaultAppDataProvider() {
            override val logTag: String
                get() = "AlipayXposed"
            override val appR: Class<out Any>
                get() = R::class.java
            override val itemModelBRid: Int
                get() = BR.model
        })

        L.init(this)

        val dbHelper = AppDaoOpenHelper(this, BuildConfig.DB_NAME)
        dbSessionManager = AppDaoSessionManager(dbHelper)
        billDb = BillDetailsRawDbWrapper(dbSessionManager)

        Stetho.initializeWithDefaults(this)
    }

    companion object {
        lateinit var app: App

        fun getPref(context: Context): AppPreferences {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return AppPreferencesManager(AppPreferencesImpl(context, pref))
        }
    }
}