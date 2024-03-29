
import android.content.Context
import kotlin.jvm.Throws

interface PreferenceHelper {
    @Throws(PreferenceException::class)
    fun getString(context: Context?, key: String?): String?

    @Throws(PreferenceException::class)
    fun putString(
        context: Context?,
        key: String?,
        value: String?
    )

    @Throws(PreferenceException::class)
    fun removeAll(context: Context?)

    @Throws(PreferenceException::class)
    fun remove(context: Context?, key: String?)

}