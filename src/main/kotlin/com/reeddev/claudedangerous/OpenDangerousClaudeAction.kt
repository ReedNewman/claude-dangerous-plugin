package com.reeddev.claudedangerous

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.ToolWindowManager
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

class OpenDangerousClaudeAction : AnAction() {

    companion object {
        private const val TAB_NAME = "Claude Code (Dangerous)"
        private const val COMMAND = "claude --dangerously-skip-permissions"
        private const val DEBOUNCE_MS = 6000L
        private var lastOpenedMs = 0L
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        ApplicationManager.getApplication().invokeLater {
            val now = System.currentTimeMillis()
            if (now - lastOpenedMs < DEBOUNCE_MS) return@invokeLater
            lastOpenedMs = now

            val basePath = project.basePath ?: return@invokeLater
            val terminalManager = TerminalToolWindowManager.getInstance(project)
            val widget = terminalManager.createShellWidget(basePath, TAB_NAME, true, true)
            widget.sendCommandToExecute(COMMAND)
            terminalManager.toolWindow?.activate(null)
        }
    }
}
