package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

abstract class RSBooleanExpression {

    @Throws(RSExpressionParserError::class)
    abstract fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Boolean

}