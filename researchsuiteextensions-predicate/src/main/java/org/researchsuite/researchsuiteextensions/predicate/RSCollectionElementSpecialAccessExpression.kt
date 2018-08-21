package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSCollectionElementSpecialAccessExpression(
        val left: RSExpression,
        val operation: RSExpressionToken,
        val right: RSExpressionToken
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

        when (right.type) {
            RSExpressionTokenType.firstString -> {
                return l?.first()
            }

            RSExpressionTokenType.lastString -> {
                return l?.last()
            }

            RSExpressionTokenType.sizeString -> {
                return l?.size
            }

            else -> {
                throw RSExpressionParserError.invalidToken("Unsupported token for collection access${this.right.type}")
            }
        }

    }
}