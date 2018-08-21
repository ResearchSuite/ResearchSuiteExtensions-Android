package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSBooleanPredicateExpression(
        val predicate: RSExpressionToken
): RSBooleanExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Boolean {

        when(this.predicate.type) {

            RSExpressionTokenType.truepredicate -> {
                return true
            }

            RSExpressionTokenType.falsepredicate -> {
                return false
            }

            else -> {
                throw RSExpressionParserError.evaluationError("Invalid token type ${this.predicate.type} found in RSBooleanPredicateExpression")
            }

        }

    }
}