package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSCollectionElementIndexedAccessExpression(
        val left: RSExpression,
        val operation: RSExpressionToken,
        val right: RSExpression
): RSExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Any? {

        val l: Collection<*>? = {
            left.evaluate(substitutions, context)?.let {
                if (it is Array<*>) {
                    it.asList()
                }
                else if (it is Collection<*>) {
                    it
                }
                else null
            }
        }()

        val rightValue = right.evaluate(substitutions, context) as? Int
        if (rightValue == null) {
            throw RSExpressionParserError.evaluationError("RSCollectionElementIndexedAccessExpression: Right side  must evaluate to valid integer")
        }

        return l?.let {
            if (rightValue >= it.size) {
                throw RSExpressionParserError.evaluationError("RSCollectionElementIndexedAccessExpression: Right side is out of range")
            }
            else it.elementAt(rightValue)
        }

    }
}