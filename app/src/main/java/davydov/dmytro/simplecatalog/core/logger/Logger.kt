package davydov.dmytro.simplecatalog.core.logger

interface Logger {
    fun d(tag: String, msg: String)
    fun e(tag: String, error: Throwable?, message: String?)
}