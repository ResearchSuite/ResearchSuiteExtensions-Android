package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSSelfExpression(val token: RSExpressionToken): RSExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Any? {

        if (token.type != RSExpressionTokenType.selfString) {
            throw RSExpressionParserError.evaluationError("Invalid self token type ${token.type}")
        }

        if (context == null) {
            throw RSExpressionParserError.evaluationError("Null Context")
        }

        return context
    }

}