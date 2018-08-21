package org.researchsuite.researchsuiteextensions.predicate

import com.google.common.base.Optional

class RSGroupingExpression(
        val expr: RSBooleanExpression
): RSBooleanExpression() {

    override fun evaluate(substitutions: Map<String, Optional<Any>>, context: Any?): Boolean {
        return this.expr.evaluate(substitutions, context)
    }
}