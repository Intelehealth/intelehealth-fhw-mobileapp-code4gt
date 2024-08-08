package org.intelehealth.app.activities.identificationActivity.model

import com.google.gson.annotations.SerializedName
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.utilities.SessionManager

/**
 * Created by Vaghela Mithun R. on 24-06-2024 - 20:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class Village(
    val name: String?,
    @SerializedName("name-hi")
    val nameHindi: String?
) {
    override fun toString(): String {
        val sessionManager = SessionManager.getInstance(IntelehealthApplication.getAppContext())
        return if (sessionManager.appLanguage.equals("hi")) nameHindi ?: name ?: "No Value"
        else name ?: "No Value"
    }
}