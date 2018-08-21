package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional
import org.researchsuite.researchsuiteextensions.common.toMapOrNull

class RSKeypathAccessExpression(
        val left: RSExpression,
        val operation: RSExpressionToken,
        val right: RSExpressionToken
): RSExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Any? {
        val leftValue = (left.evaluate(substitutions, context) as? Map<*, *>)?.toMapOrNull<String, Optional<Any>>()

        if (leftValue == null) {
            throw RSExpressionParserError.evaluationError("RSKeypathAccessExpression only supports values of type Map<String, Optional<Any>>")
        }

        if (operation.type != RSExpressionTokenType.dot) {
            throw RSExpressionParserError.evaluationError("Invalid operation ${operation.type}")
        }

        if (right.type != RSExpressionTokenType.implicitVariable) {
            throw RSExpressionParserError.evaluationError("Invalid keypath type ${right.type}")
        }

        val key = right.value as? String
        if (key == null) {
            throw RSExpressionParserError.evaluationError("Invalid keypath value ${right.value}")
        }

        return leftValue.get(key)

    }

}