package com.i19097842.curtin.edu.au.mad_assignment2

import android.app.Application
import android.content.Context

/**
 * Provides access to the application context outside of an activity. e.g in a model
 *
 * @author Seain Malkin (19097842@student.curtin.edu.au)
 */
class GameApp : Application() {
    /** Save a reference to the application process */
    init {
        instance = this
    }

    companion object {
        /** @property[instance] Singleton reference to application */
        private var instance: GameApp? = null

        /**
         * Provides external access to application context
         */
        fun getContext(): Context {
            return instance!!.applicationContext
        }
    }
}