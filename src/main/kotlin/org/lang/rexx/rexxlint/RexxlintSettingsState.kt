package org.lang.rexx.rexxlint

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service(Service.Level.APP)
@State(name = "RexxlintSettings", storages = [Storage("rexxlint.xml")])
class RexxlintSettingsState : PersistentStateComponent<RexxlintSettingsState.State> {
    data class State(
        var executablePath: String = "",
    )

    private var state = State()

    var executablePath: String
        get() = state.executablePath
        set(value) {
            state.executablePath = value.trim()
        }

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }
}
