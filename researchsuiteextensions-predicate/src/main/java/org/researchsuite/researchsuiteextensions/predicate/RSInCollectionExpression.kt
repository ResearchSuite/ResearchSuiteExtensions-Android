package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSInCollectionExpression(
        val left: RSExpression,
        val operation: RSExpressionToken,
        val right: RSExpression
): RSBooleanExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Boolean {

        val leftValue = this.left.evaluate(substitutions, context)
        val rightValue = this.right.evaluate(substitutions, context)

        if (rightValue !is List<*>) {
            throw RSExpressionParserError.evaluationError("RSImplicitVariableExpression requires right value of type List")
        }

        return rightValue.contains(leftValue)

    }
}