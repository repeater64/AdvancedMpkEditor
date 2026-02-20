package me.repeater64.advancedmpkeditor.backend.commands

class CircularConditionsException(val barrelName: String, val problematicLabels: String) : RuntimeException()