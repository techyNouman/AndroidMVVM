import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import kotlin.jvm.Throws

class AppPreference() : PreferenceHelper {


    //Preference file name.
    private val PREFS_FILE_NAME = "PREF"
    val DEFAULT_STRING = ""
    val DEFAULT_BOOLEAN_VALUE = false


    /**
     * Getter to retrieve the singleton instance.
     *
     * @return [AppPreference] instance.
     */
    companion object {
        //Pref Singleton instance.
        private var mInstance: AppPreference? = null

        //Dummy object for locking.
        val lock = Any()

        fun getInstance(): AppPreference? {
            if (mInstance == null) {
                synchronized(lock) {
                    if (mInstance == null) {
                        mInstance =
                            AppPreference()
                    }
                }
            }
            return mInstance
        }
    }

    /**
     * Returns the [SharedPreferences] object.
     *
     * @param context The context.
     * @param mode    The type of mode. One of the values in [Context.MODE_PRIVATE] or [Context.MODE_APPEND].
     * @return [SharedPreferences] for this application.
     */
    private fun getPreferences(
        context: Context,
        mode: Int
    ): SharedPreferences? {
        return context.getSharedPreferences(
            PREFS_FILE_NAME,
            mode
        )
    }

    override fun getString(context: Context?, key: String?): String? {
        val sharedPreferences = getPreferences(context!!, Context.MODE_PRIVATE)
        return if (sharedPreferences != null) {
            sharedPreferences.getString(
                key, DEFAULT_STRING
            )
        } else {
            throw PreferenceException(
                PreferenceException.PREFERENCE_NOT_FOUND
            )
        }
    }


    @Throws(PreferenceException::class)
    override fun putString(
        context: Context?,
        key: String?,
        value: String?
    ) {
        val sharedPreferences =
            getPreferences(context!!, Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            val editor = sharedPreferences.edit()
            if (editor != null) {
                editor.putString(key, value)
            } else {
                throw PreferenceException(
                    PreferenceException.PREFERENCE_EDIT_PROHIBITED
                )
            }
            editor.apply()
        } else {
            throw PreferenceException(
                PreferenceException.PREFERENCE_NOT_FOUND
            )
        }
    }


    override fun removeAll(context: Context?) {
        val preferences =
            getPreferences(context!!, Context.MODE_PRIVATE)
        val editor = preferences!!.edit()
        editor.clear()
        editor.apply()
    }

    override fun remove(context: Context?, key: String?) {
        val sharedPreferences =
            getPreferences(context!!, Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            val editor = sharedPreferences.edit()
            if (editor != null) {
                editor.remove(key)
            } else {
                throw PreferenceException(
                    PreferenceException.PREFERENCE_EDIT_PROHIBITED
                )
            }
            editor.apply()
        } else {
            throw PreferenceException(
                PreferenceException.PREFERENCE_NOT_FOUND
            )
        }
    }
    fun <T> put(context: Context?, `object`: T, key: String) {
        val preferences =
            getPreferences(context!!, Context.MODE_PRIVATE)
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        preferences!!.edit().putString(key, jsonString).apply()
    }


    inline fun <reified T> get(context: Context?, key: String): T? {
        val sharedPref = context!!.getSharedPreferences("PREF_S9_CUSTOMER", Context.MODE_PRIVATE)
        //We read JSON String which was saved.
        val value = sharedPref!!.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}