package com.philipgurr.kpref

import android.content.Context

/**
 * Classes implementing this interface have to provide an android context.
 */
interface ContextProvider {
    /**
     * The context to be used for the preferences.
     */
    val context: Context
}