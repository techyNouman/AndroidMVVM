
class PreferenceException(message: String?) : Exception(){
    private var authToken: String? = null

    companion object {
        val PREFERENCE_NOT_FOUND = "Preference not found"
        val PREFERENCE_EDIT_PROHIBITED = "Editing the preference is not allowed"
    }

    init {
        authToken = message
    }

}