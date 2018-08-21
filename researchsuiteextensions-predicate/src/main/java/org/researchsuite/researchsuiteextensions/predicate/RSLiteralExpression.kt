package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSLiteralExpression(val token: RSExpressionToken): RSExpression() {

    val literal: Any? = {
        when(token.type) {
            RSExpressionTokenType.intLiteral, RSExpressionTokenType.stringLiteral -> {
                if (token.value == null) {
                    throw RSExpressionParserError.invalidToken("Invalid literal token ${token}")
                }
                else {
                    token.value
                }
            }

            RSExpressionTokenType.yesString, RSExpressionTokenType.trueString -> {
                true
            }

            RSExpressionTokenType.noString, RSExpressionTokenType.falseString -> {
                false
            }

            RSExpressionTokenType.nilString, RSExpressionTokenType.nullString -> {
                 null
            }

            else -> {
                throw RSExpressionParserError.invalidToken("Invalid literal token ${token}")
            }

        }
    }()

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Any? {
        return this.literal
    }
}