package ourcorner.pushtest

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)

        showTokenAndSetupForUpdates()

        setupSubscribe()

        setupUnsubscribe()
    }

    private fun showTokenAndSetupForUpdates() {
        applicationContext.getSharedPreferences("default", Context.MODE_PRIVATE)
            .also {
                token.text = it.getString(TOKEN_KEY, getString(R.string.token_error))
            }
            .registerOnSharedPreferenceChangeListener { prefs, key ->
                if (key == TOKEN_KEY) {
                    token.text = prefs.getString(key, getString(R.string.token_error))

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        R.string.token_updated,
                        Snackbar.LENGTH_INDEFINITE
                    ).also { snackbar ->
                        snackbar.setAction("Ok") { snackbar.dismiss() }
                    }.show()
                }
            }
    }

    private fun setupSubscribe() {
        subscribe.setOnClickListener {
            this.hideKeyboard()
            if (!edit_text_topic.text.isNullOrBlank()) {
                FirebaseMessaging.getInstance().subscribeToTopic(edit_text_topic.text.toString())
                    .addOnCompleteListener {
                        showSnackbar(
                            if (!it.isSuccessful) {
                                getString(R.string.msg_subscribe_failed)
                            } else {
                                getString(R.string.msg_subscribed)
                            }
                        )
                    }
            } else {
                showSnackbar(getString(R.string.topic_empty))
            }
        }
    }

    private fun setupUnsubscribe() {
        unsubscribe.setOnClickListener {
            this.hideKeyboard()
            if (!edit_text_topic.text.isNullOrBlank()) {
                FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(edit_text_topic.text.toString())
                    .addOnCompleteListener {
                        showSnackbar(
                            if (!it.isSuccessful) {
                                getString(R.string.msg_unsubscribe_failed)
                            } else {
                                getString(R.string.msg_unsubscribed)
                            }
                        )
                    }
            } else {
                showSnackbar(getString(R.string.topic_empty))
            }
        }
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            msg,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val TOKEN_KEY = "token"
    }
}
