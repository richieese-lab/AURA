package com.aura.app.core.model

/**
 * Peran akun AURA. Hanya dua peran (D-14): Manajer sudah dihapus.
 */
enum class Peran {
    OWNER,
    KASIR;

    /** Hanya Owner yang memakai bottom navigation; Kasir langsung masuk menu Kasir. */
    val punyaBottomNav: Boolean get() = this == OWNER
}
