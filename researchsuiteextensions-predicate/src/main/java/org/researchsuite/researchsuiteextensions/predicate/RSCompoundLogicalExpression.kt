package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSCompoundLogicalExpression(
        val left: RSBooleanExpression,
        val op: RSExpressionToken,
        val right: RSBooleanExpression):
        RSBooleanExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Boolean {

        when(this.op.type) {

            RSExpressionTokenType.andString, RSExpressionTokenType.doubleAmp -> {
                return this.left.evaluate(substitutions, context) && this.right.evaluate(substitutions, context)
            }

            RSExpressionTokenType.orString, RSExpressionTokenType.doublePipe -> {
                return this.left.evaluate(substitutions, context) || this.right.evaluate(substitutions, context)
            }

            else -> {
                throw RSExpressionParserError.evaluationError("Invalid operation type ${this.op.type} found in RSCompoundLogicalExpression")
            }

        }

    }
}