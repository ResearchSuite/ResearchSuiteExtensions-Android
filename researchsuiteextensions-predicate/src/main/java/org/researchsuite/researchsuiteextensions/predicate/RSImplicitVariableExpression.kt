package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional
import org.researchsuite.researchsuiteextensions.common.toMapOrNull


class RSImplicitVariableExpression(val token: RSExpressionToken): RSExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Any? {

        if (token.type != RSExpressionTokenType.implicitVariable) {
            throw RSExpressionParserError.evaluationError("Invalid explicit variable token type ${token.type}")
        }

        val obj = (context as? Map<*, *>)?.toMapOrNull<String, Optional<Any>>()

        if (obj == null) {
            throw RSExpressionParserError.evaluationError("RSImplicitVariableExpression requires context of type Map<String, Optional<Any>>")
        }

        val variableName = token.value as? String
        if (variableName == null) {
            throw RSExpressionParserError.evaluationError("Invalid variable name ${token.value}")
        }

        return obj.get(variableName)
    }
}