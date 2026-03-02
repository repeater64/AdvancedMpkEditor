package me.repeater64.advancedmpkeditor.backend.data_object

interface CopyPasteable<T> {
    fun copyInto(other: T)
    fun typeMatches(other: Any) : Boolean
}