package io.bkraszewski.safecam.feature

import android.content.Context

interface StringProvider {
    fun provideString(stringId : Int) : String
}

class StringProviderImpl constructor(private val context: Context) : StringProvider {
    override fun provideString(stringId: Int) : String {
        return context.getString(stringId)
    }
}
