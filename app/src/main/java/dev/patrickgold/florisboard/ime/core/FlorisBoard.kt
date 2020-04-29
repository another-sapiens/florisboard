package dev.patrickgold.florisboard.ime.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.media.AudioManager
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.preference.PreferenceManager
import dev.patrickgold.florisboard.R
import dev.patrickgold.florisboard.ime.key.KeyCode
import dev.patrickgold.florisboard.ime.key.KeyData
import dev.patrickgold.florisboard.ime.key.KeyType
import dev.patrickgold.florisboard.ime.keyboard.KeyboardMode
import dev.patrickgold.florisboard.ime.keyboard.KeyboardView
import dev.patrickgold.florisboard.ime.layout.LayoutManager
import dev.patrickgold.florisboard.ime.smartbar.SmartbarManager
import dev.patrickgold.florisboard.settings.SettingsMainActivity
import dev.patrickgold.florisboard.util.initDefaultPreferences
import java.util.*

class FlorisBoard : InputMethodService() {

    private var activeKeyboardMode: KeyboardMode? = null
    private var hasCapsRecentlyChanged: Boolean = false
    private val keyboardViews = EnumMap<KeyboardMode, KeyboardView>(
        KeyboardMode::class.java)
    private val osHandler = Handler()

    var caps: Boolean = false
    var capsLock: Boolean = false

    var audioManager: AudioManager? = null
    val layoutManager = LayoutManager(this)
    var prefs: PrefCache? = null
        private set
    val smartbarManager: SmartbarManager = SmartbarManager(this)

    @SuppressLint("InflateParams")
    override fun onCreateInputView(): View? {
        // Set default preference values if user has not used preferences screen
        initDefaultPreferences(this)

        val rootView = layoutInflater.inflate(R.layout.florisboard, null) as LinearLayout
        layoutManager.autoFetchAssociationsFromPrefs()
        prefs = PrefCache(rootView.context, PreferenceManager.getDefaultSharedPreferences(rootView.context))
        prefs!!.sync()
        for (mode in KeyboardMode.values()) {
            val keyboardView = KeyboardView(rootView.context, this)
            rootView.addView(keyboardView)
            keyboardView.visibility = View.GONE
            keyboardView.setKeyboardMode(mode)
            keyboardViews[mode] = keyboardView
        }
        rootView.findViewById<LinearLayout>(R.id.keyboard_preview).visibility = View.GONE
        setActiveKeyboardMode(KeyboardMode.CHARACTERS)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return rootView
    }

    override fun onCreateCandidatesView(): View {
        return smartbarManager.createSmartbarView()
    }

    override fun onInitializeInterface() {
        super.onInitializeInterface()
        prefs?.sync() ?: return
    }

    private fun setActiveKeyboardMode(mode: KeyboardMode) {
        keyboardViews[activeKeyboardMode]?.visibility = View.GONE
        keyboardViews[mode]?.visibility = View.VISIBLE
        activeKeyboardMode = mode
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        setCandidatesViewShown(true)
        super.onStartInputView(info, restarting)
    }

    fun sendKeyPress(keyData: KeyData) {
        val ic = currentInputConnection
        if (keyData.type == KeyType.CHARACTER) {
            var text = keyData.code.toChar()
            if (caps) {
                text = text.toUpperCase()
            }
            ic.commitText(text.toString(), 1)
            if (!capsLock) {
                caps = false
            }
        } else {
            when (keyData.code) {
                KeyCode.DELETE -> ic.deleteSurroundingText(1, 0)
                KeyCode.ENTER -> {
                    val action = currentInputEditorInfo.imeOptions
                    Log.d("imeOptions", action.toString())
                    Log.d("imeOptions action only", (action and 0xFF).toString())
                    if (action and EditorInfo.IME_FLAG_NO_ENTER_ACTION > 0) {
                        ic.sendKeyEvent(
                            KeyEvent(
                                KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_ENTER
                            )
                        )
                    } else {
                        when (action and EditorInfo.IME_MASK_ACTION) {
                            EditorInfo.IME_ACTION_DONE,
                            EditorInfo.IME_ACTION_GO,
                            EditorInfo.IME_ACTION_NEXT,
                            EditorInfo.IME_ACTION_PREVIOUS,
                            EditorInfo.IME_ACTION_SEARCH,
                            EditorInfo.IME_ACTION_SEND -> {
                                ic.performEditorAction(action)
                            }
                            else -> {
                                ic.sendKeyEvent(
                                    KeyEvent(
                                        KeyEvent.ACTION_DOWN,
                                        KeyEvent.KEYCODE_ENTER
                                    )
                                )
                            }
                        }
                    }
                }
                KeyCode.LANGUAGE_SWITCH -> {
                    val i = Intent(this, SettingsMainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                }
                KeyCode.SHIFT -> {
                    if (hasCapsRecentlyChanged) {
                        osHandler.removeCallbacksAndMessages(null)
                        caps = true
                        capsLock = true
                        hasCapsRecentlyChanged = false
                    } else {
                        caps = !caps
                        capsLock = false
                        hasCapsRecentlyChanged = true
                        osHandler.postDelayed({
                            hasCapsRecentlyChanged = false
                        }, 300)
                    }
                }
                KeyCode.SWITCH_TO_NEXT_IME -> {android.util.Log.i("SW", "SWITCH")
                    switchInputMethod(null)
                }
                KeyCode.VIEW_CHARACTERS -> setActiveKeyboardMode(KeyboardMode.CHARACTERS)
                KeyCode.VIEW_NUMERIC -> setActiveKeyboardMode(KeyboardMode.NUMERIC)
                KeyCode.VIEW_SYMBOLS -> setActiveKeyboardMode(KeyboardMode.SYMBOLS)
                KeyCode.VIEW_SYMBOLS2 -> setActiveKeyboardMode(KeyboardMode.SYMBOLS2)
            }
        }
    }
}
