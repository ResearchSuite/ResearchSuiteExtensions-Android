package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSNegativeLogicalExpression(
        val op: RSExpressionToken,
        val right: RSBooleanExpression
): RSBooleanExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Boolean {

        when(this.op.type) {

            RSExpressionTokenType.bang -> {
                return !this.right.evaluate(substitutions, context)
            }

            else -> {
                throw RSExpressionParserError.evaluationError("Invalid operation type ${this.op.type} found in RSNegativeLogicalExpression")
            }

        }

    }
}