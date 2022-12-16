package davydov.dmytro.simplecatalog.core

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import davydov.dmytro.simplecatalog.R

const val PLACEHOLDER_URL = "via.placeholder.com"
const val USER_AGENT_HEADER_KEY = "User-Agent"
const val USER_AGENT_HEADER_VALUE = "1"

@SuppressLint("CheckResult")
fun ImageView.loadImage(
    url: String?,
    @DrawableRes loadingPlaceholder: Int? = R.drawable.image_placeholder,
    @DrawableRes errorPlaceholder: Int? = R.drawable.image_placeholder
) {
    url?.let { urlNotNull ->
        val requestManager = Glide.with(this)
        //workaround for Glide to use via.placeholder.com images
        //Glide could not load them without user agent
        val requestBuilder = if (urlNotNull.contains(PLACEHOLDER_URL)) {
            val customUrl = GlideUrl(
                urlNotNull,
                LazyHeaders.Builder()
                    .addHeader(USER_AGENT_HEADER_KEY, USER_AGENT_HEADER_VALUE)
                    .build()
            )
            requestManager.load(customUrl)
        } else {
            requestManager.load(urlNotNull)
        }
        loadingPlaceholder?.let(requestBuilder::placeholder)
        errorPlaceholder?.let(requestBuilder::error)
        requestBuilder.into(this)
    }
}