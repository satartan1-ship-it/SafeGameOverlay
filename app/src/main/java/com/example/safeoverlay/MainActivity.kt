package com.example.safeoverlay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 28, 24, 28)
            setBackgroundColor(0xFF10141C.toInt())
        }

        fun text(value: String, size: Float, color: Int = 0xFFFFFFFF.toInt()) =
            TextView(this).apply {
                text = value
                textSize = size
                setTextColor(color)
                setPadding(0, 8, 0, 8)
            }

        content.addView(text("POTATO MODE · REDMI NOTE 13", 23f))
        content.addView(text(
            "Быстрый запуск Standoff 2 через Game Turbo и инструкция для минимальной нагрузки.",
            15f, 0xFFB9C3D2.toInt()
        ))

        content.addView(text("1. ДОБАВЬ STANDOFF 2 В GAME TURBO", 18f))
        content.addView(text(
            "Открой приложение «Безопасность» → Game Turbo → «Добавить игру» → Standoff 2. " +
            "Затем запускай игру из Game Turbo, чтобы игровой режим применялся.",
            15f
        ))
        content.addView(actionButton("ОТКРЫТЬ «БЕЗОПАСНОСТЬ» / GAME TURBO") {
            openPackage("com.miui.securitycenter", "Не удалось открыть «Безопасность». Открой её вручную.")
        })

        content.addView(text("2. НАСТРОЙ GPU (ЕСЛИ ПУНКТ ДОСТУПЕН)", 18f))
        content.addView(text(
            "В Game Turbo открой Standoff 2 → «По умолчанию» или «Настроить» → GPU settings / Настройки GPU. " +
            "Выбери Custom / Пользовательский и поставь минимальное разрешение, выключи Anti-aliasing " +
            "(сглаживание) и оставь минимальное качество фильтрации текстур. Если таких пунктов нет — " +
            "твоя версия Game Turbo их не поддерживает.",
            15f
        ))

        content.addView(text("3. В САМОЙ ИГРЕ", 18f))
        content.addView(text(
            "• Графика: низкая\n• Тени и дополнительные эффекты: выкл.\n" +
            "• Сглаживание: выкл. (если есть)\n• FPS: максимальный стабильный",
            15f
        ))

        content.addView(text("4. ЗАПУСК", 18f))
        content.addView(actionButton("ЗАПУСТИТЬ STANDOFF 2") { launchGame() })
        content.addView(actionButton("НАСТРОЙКИ БАТАРЕИ") {
            runCatching { startActivity(Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)) }
                .onFailure { toast("Настройки батареи недоступны") }
        })

        content.addView(text(
            "Честно: Android не даёт обычному приложению менять внутренние графические параметры Standoff 2. " +
            "Эта версия открывает штатный Game Turbo и ведёт к доступным настройкам Xiaomi; " +
            "она не внедряет DLL и не меняет файлы игры.",
            13f, 0xFF8F9BAD.toInt()
        ))

        val scroll = ScrollView(this).apply {
            addView(content, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ))
        }
        setContentView(scroll)
    }

    private fun actionButton(label: String, action: () -> Unit) =
        Button(this).apply {
            text = label
            isAllCaps = false
            textSize = 14f
            setOnClickListener { action() }
        }

    private fun openPackage(packageName: String, error: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent == null) toast(error) else startActivity(intent)
    }

    private fun launchGame() {
        openPackage("com.axlebolt.standoff2", "Standoff 2 не найдена. Установи игру из официального магазина.")
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
