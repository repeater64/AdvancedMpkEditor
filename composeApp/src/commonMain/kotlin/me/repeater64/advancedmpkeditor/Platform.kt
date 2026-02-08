package me.repeater64.advancedmpkeditor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform