package davydov.dmytro.simplecatalog.core.logger

import android.util.Log
import davydov.dmytro.simplecatalog.BuildConfig

class LoggerImpl : Logger {

    override fun d(tag: String, msg: String) {
        logDebugOnly { Log.d(tag, msg) }
    }

    override fun e(tag: String, error: Throwable?, message: String?) {
        logDebugOnly { Log.e(tag, message, error) }
    }

    private inline fun logDebugOnly(block: () -> Unit) {
        if (BuildConfig.DEBUG) {
            block()
        }
    }
}