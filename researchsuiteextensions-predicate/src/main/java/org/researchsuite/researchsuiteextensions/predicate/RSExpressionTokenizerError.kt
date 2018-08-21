package org.researchsuite.researchsuiteextensions.predicate

sealed class RSExpressionTokenizerError(message: String? = null): Error(message) {

    class malformedInteger: RSExpressionTokenizerError()
    class unrecognizedCharacter(message: Char): RSExpressionTokenizerError(message.toString())
    class unterminatedString: RSExpressionTokenizerError()
    class unexpectedCharacter(message: Char): RSExpressionTokenizerError(message.toString())

}