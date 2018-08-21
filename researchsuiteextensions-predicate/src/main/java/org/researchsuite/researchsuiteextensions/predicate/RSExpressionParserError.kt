package org.researchsuite.researchsuiteextensions.predicate

sealed class RSExpressionParserError(message: String): Error(message) {

    class invalidToken(message: String): RSExpressionParserError(message)
    class evaluationError(message: String): RSExpressionParserError(message)

}

