package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSPredicateExpression(
        val rootExpression: RSBooleanExpression,
        val substitutions: Map<String, Optional<Any>>? = null
): RSBooleanExpression() {

    companion object {
        @Throws(RSExpressionParserError::class, RSExpressionTokenizerError::class)
        fun generateExpression(format: String): RSPredicateExpression {
            val tokens = RSExpressionTokenizer.generateTokens(format)
            val rootExpression = RSExpressionParser.generateExpression(tokens)
            return RSPredicateExpression(rootExpression)
        }
    }

    fun withSubstitutions(substitutions: Map<String, Optional<Any>>): RSPredicateExpression {
        return RSPredicateExpression(rootExpression, substitutions)
    }

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Boolean {
        return this.rootExpression.evaluate(substitutions, context)
    }

    @Throws(RSExpressionParserError::class)
    fun evaluate(context: Any?): Boolean {
        val substitutions = this.substitutions ?: mapOf()
        return this.evaluate(substitutions, context)
    }

    @Throws(RSExpressionParserError::class)
    fun filter(l: List<Any>): List<Any> {

        val filteredList = l.filter { element ->

            assert(element is Map<*,*>)
            if(element is Map<*,*>) {
                this.evaluate(element)
            }
            else {
                false
            }


        }

        return filteredList

    }

}