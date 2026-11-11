package com.example.safeoverlay

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
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

        content.addView(text("POTATO MODE", 27f))
        content.addView(text(
            "Профиль низкой графики для Standoff 2\n\n" +
            "Приложение не использует плавающие окна, не читает данные игры и не меняет её файлы. " +
            "Настройки ниже нужно применить в самой игре.",
            15f, 0xFFB9C3D2.toInt()
        ))

        content.addView(text("НАСТРОЙКИ В ИГРЕ", 18f))
        content.addView(text(
            "• Качество графики: низкое\n" +
            "• Тени: выключить\n" +
            "• Сглаживание: выключить\n" +
            "• Эффекты: минимум\n" +
            "• Разрешение: минимальное доступное\n" +
            "• Частота кадров: максимальная стабильная для устройства",
            16f
        ))

        content.addView(text("ПЕРЕД ЗАПУСКОМ", 18f))
        content.addView(text(
            "• Закрой тяжёлые приложения\n" +
            "• Отключи энергосбережение, если оно ограничивает производительность\n" +
            "• Дай телефону остыть, если он нагрелся",
            16f
        ))

        content.addView(actionButton("ЗАПУСТИТЬ STANDOFF 2") { launchGame() })
        content.addView(actionButton("ОТКРЫТЬ НАСТРОЙКИ ЭКРАНА") {
            runCatching { startActivity(Intent(Settings.ACTION_DISPLAY_SETTINGS)) }
                .onFailure { toast("Настройки экрана недоступны") }
        })
        content.addView(actionButton("ОТКРЫТЬ НАСТРОЙКИ БАТАРЕИ") {
            runCatching { startActivity(Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)) }
                .onFailure { toast("Настройки батареи недоступны") }
        })
        content.addView(text(
            "Важно: Android не разрешает стороннему приложению напрямую переключать графические параметры Standoff 2. " +
            "Этот профиль помогает быстро выставить их вручную; прирост FPS не гарантируется.",
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
            textSize = 15f
            setOnClickListener { action() }
        }

    private fun launchGame() {
        val intent = packageManager.getLaunchIntentForPackage("com.axlebolt.standoff2")
        if (intent == null) {
            toast("Standoff 2 не найдена. Установи игру из официального магазина.")
        } else {
            startActivity(intent)
        }
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
