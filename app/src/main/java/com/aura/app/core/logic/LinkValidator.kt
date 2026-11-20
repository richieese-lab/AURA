package com.aura.app.core.logic

import java.net.URI

/** Link supplier harus berupa URL valid (spec/06 Detail Supplier). */
object LinkValidator {

    fun valid(link: String): Boolean {
        val teks = link.trim()
        if (teks.isEmpty() || teks.any { it.isWhitespace() }) return false
        return try {
            val uri = URI(teks)
            val skema = uri.scheme?.lowercase()
            (skema == "http" || skema == "https") && !uri.host.isNullOrBlank()
        } catch (e: Exception) {
            false
        }
    }
}
