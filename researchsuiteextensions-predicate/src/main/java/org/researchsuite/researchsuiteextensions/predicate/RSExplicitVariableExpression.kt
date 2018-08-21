package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSExplicitVariableExpression(val token: RSExpressionToken): RSExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Any? {

        if (token.type != RSExpressionTokenType.explicitVariable) {
            throw RSExpressionParserError.evaluationError("Invalid explicit variable token type ${token.type}")
        }

        val variableName = token.value as? String
        if (variableName == null) {
            throw RSExpressionParserError.evaluationError("Invalid variable name ${token.value}")
        }

        val optionalValue = substitutions.get(variableName)
        if (optionalValue == null) {
            throw RSExpressionParserError.evaluationError("Variable ${variableName} not defined in substitutions")
        }

        return if (optionalValue.isPresent) optionalValue.get() else null
    }
}