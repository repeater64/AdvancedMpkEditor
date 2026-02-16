package me.repeater64.advancedmpkeditor.backend.data_object.randomiser

interface WeightedOptionEitherType<T> {
    var option: T
    var weight: Int
}