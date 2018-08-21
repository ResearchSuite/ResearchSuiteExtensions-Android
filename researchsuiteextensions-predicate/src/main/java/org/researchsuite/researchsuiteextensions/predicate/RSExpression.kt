package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional
import java.util.*

abstract class RSExpression {

    //TODO: possibly change substituions map to Map<String, Optional<Any>>

    @Throws(RSExpressionParserError::class)
    abstract fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Any?

    @Throws(RSExpressionParserError::class)
    fun equals(other: RSExpression, substitutions: Map<String, Optional<Any>>, context: Any?): Boolean {

        val value = this.evaluate(substitutions, context)
        val otherValue = other.evaluate(substitutions, context)

        //note that this is equivalent to value?.equals(otherValue)
        return value == otherValue

    }

    @Throws(RSExpressionParserError::class)
    fun greaterThan(other: RSExpression, substitutions: Map<String, Optional<Any>>, context: Any?): Boolean {

        val value = this.evaluate(substitutions, context)
        val otherValue = other.evaluate(substitutions, context)

        when(value) {
            is String -> {

                if (otherValue is String) {
                    return value > otherValue
                }
                else {
                    throw RSExpressionParserError.evaluationError("incompatible types")
                }

            }

            is Int -> {

                if (otherValue is Int) {
                    return value > otherValue
                }
                else {
                    throw RSExpressionParserError.evaluationError("incompatible types")
                }

            }

            is Date -> {

                if (otherValue is Date) {
                    return value > otherValue
                }
                else {
                    throw RSExpressionParserError.evaluationError("incompatible types")
                }

            }

            else -> {
                throw RSExpressionParserError.evaluationError("incompatible types")
            }
        }


    }

}