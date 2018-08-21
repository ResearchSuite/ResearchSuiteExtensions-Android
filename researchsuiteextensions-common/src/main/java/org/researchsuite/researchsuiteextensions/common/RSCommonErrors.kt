package org.researchsuite.researchsuiteextensions.common

sealed class RSCommonErrors(message: String? = null): Error(message) {

    class ConversionError(): RSCommonErrors()

}