package com.pluu.kotlin

/**
 * Custom Extends
 * Created by pluu on 2017-04-17.
 */
fun <T : Any?> T?.checkNullOrElse(E: T) = if (this != null) this else E