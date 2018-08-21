package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSComparisonExpression(
        val left: RSExpression,
        val operation: RSExpressionToken,
        val right: RSExpression
): RSBooleanExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Boolean {

        when(this.operation.type) {
            RSExpressionTokenType.equal, RSExpressionTokenType.equalEqual -> {
                return this.left.equals(this.right, substitutions, context)
            }

            RSExpressionTokenType.bangNotEqual, RSExpressionTokenType.angleNotEqual -> {
                return !this.left.equals(this.right, substitutions, context)
            }

            RSExpressionTokenType.gte, RSExpressionTokenType.egt -> {
                return this.left.greaterThan(this.right, substitutions, context) ||
                        this.left.equals(this.right, substitutions, context)
            }

            RSExpressionTokenType.gt -> {
                return this.left.greaterThan(this.right, substitutions, context)
            }

            RSExpressionTokenType.lte, RSExpressionTokenType.elt -> {
                return !this.left.greaterThan(this.right, substitutions, context)
            }

            RSExpressionTokenType.lt -> {
                return !(this.left.greaterThan(this.right, substitutions, context) ||
                        this.left.equals(this.right, substitutions, context))
            }

            else -> {
                throw RSExpressionParserError.evaluationError("Cannot compare $left and $right via $operation")
            }
        }


    }

}