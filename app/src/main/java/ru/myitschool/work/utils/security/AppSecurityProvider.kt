package ru.myitschool.work.utils.security

import com.google.crypto.tink.Aead
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import ru.myitschool.work.App

object AppSecurityProvider {
    fun provideAead(): Aead {
        val keysetManager = AndroidKeysetManager.Builder()
            .withSharedPref(App.context, "tink_keyset", "tink_prefs")
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withMasterKeyUri("android-keystore://master_key")
            .build()

        return keysetManager.keysetHandle.getPrimitive(
            RegistryConfiguration.get(),
            Aead::class.java
        )
    }
}
