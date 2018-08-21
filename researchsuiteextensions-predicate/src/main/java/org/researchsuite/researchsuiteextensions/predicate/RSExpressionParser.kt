package org.researchsuite.researchsuiteextensions.predicate

class RSExpressionParser(val tokens: List<RSExpressionToken>) {
    var current = 0

    companion object {
        @Throws(RSExpressionParserError::class)
        fun generateExpression(tokens: List<RSExpressionToken>): RSBooleanExpression {

            val parser = RSExpressionParser(tokens)
            return parser.parseBooleanExpression()

        }
    }

    val currentToken: RSExpressionToken
        get() = this.tokens[this.current]

    val previousToken: RSExpressionToken
        get() = this.tokens[this.current - 1]

    val isAtEnd: Boolean
        get() = this.currentToken.type == RSExpressionTokenType.eof

    fun advance(): RSExpressionToken {
        if (!this.isAtEnd) { this.current = this.current + 1 }
        return this.previousToken
    }

    fun check(tokenType: RSExpressionTokenType): Boolean {
        if (this.isAtEnd) { return false }
        return this.currentToken.type == tokenType
    }

    @Throws(RSExpressionParserError::class)
    fun consume(tokenType: RSExpressionTokenType): RSExpressionToken {
        if (this.check(tokenType)) { return this.advance() }
        else { throw RSExpressionParserError.invalidToken("Expecting $tokenType, got ${this.currentToken.type} ") }
    }

    fun match(tokenTypes: List<RSExpressionTokenType>): Boolean {

        if (this.isAtEnd) return false

        if (tokenTypes.contains(this.currentToken.type)) {
            this.advance()
            return true
        }
        else {
            return false
        }

    }

    @Throws(RSExpressionParserError::class)
    fun parseBooleanExpression(): RSBooleanExpression {
        return this.booleanExpression()
    }


    @Throws(RSExpressionParserError::class)
    fun booleanExpression(): RSBooleanExpression {

        var expr = this.orTermExpression()
        while(match(listOf(RSExpressionTokenType.orString, RSExpressionTokenType.doublePipe))) {

            val op = this.previousToken
            val right = this.orTermExpression()

            expr = RSCompoundLogicalExpression(expr, op, right)

        }

        return expr

    }

    @Throws(RSExpressionParserError::class)
    fun orTermExpression(): RSBooleanExpression {

        var expr = this.unaryExpression()
        while(match(listOf(RSExpressionTokenType.andString, RSExpressionTokenType.doubleAmp))) {

            val op = this.previousToken
            val right = this.orTermExpression()

            expr = RSCompoundLogicalExpression(expr, op, right)

        }

        return expr


    }

    @Throws(RSExpressionParserError::class)
    fun unaryExpression(): RSBooleanExpression {

        if (match(listOf(RSExpressionTokenType.bang))) {
            val op = this.previousToken
            val right = this.unaryExpression()
            return RSNegativeLogicalExpression(op, right)
        }
        else {
            return this.primaryExpression()
        }

    }

    @Throws(RSExpressionParserError::class)
    fun primaryExpression(): RSBooleanExpression {

        //check boolean predicates
        if (match(listOf(RSExpressionTokenType.truepredicate, RSExpressionTokenType.falsepredicate))) {
            return RSBooleanPredicateExpression(this.previousToken)
        }
        else if (match(listOf(RSExpressionTokenType.leftParen))) {
            val expr = this.booleanExpression()
            this.consume(RSExpressionTokenType.rightParen)
            return RSGroupingExpression(expr)
        }
        else {
            val saveCurrent = this.current

            try {
                val expr = this.comparisonPredicateExpression()
                return expr
            } catch (e: RSExpressionParserError) {
                this.current = saveCurrent
            }

            try {
                val expr = this.stringPredicateExpression()
                return expr
            } catch (e: RSExpressionParserError) {
                this.current = saveCurrent
            }

            try {
                val expr = this.inCollectionPredicateExpression()
                return expr
            } catch (e: RSExpressionParserError) {
                this.current = saveCurrent
            }




            throw RSExpressionParserError.invalidToken("Could not match ${this.currentToken} as a primary expression")
        }

    }

    @Throws(RSExpressionParserError::class)
    fun comparisonPredicateExpression(): RSBooleanExpression {
        val left = expression()
        val op = comparisonOperation()
        val right = expression()

        return RSComparisonExpression(left, op, right)
    }

    @Throws(RSExpressionParserError::class)
    fun expression(): RSExpression {

        if (match(listOf(
                        RSExpressionTokenType.stringLiteral,
                        RSExpressionTokenType.intLiteral,
                        RSExpressionTokenType.falseString, RSExpressionTokenType.noString,
                        RSExpressionTokenType.trueString, RSExpressionTokenType.yesString,
                        RSExpressionTokenType.nilString, RSExpressionTokenType.nullString
                ))) {
            return RSLiteralExpression(this.previousToken)
        }

        else if (match(listOf(RSExpressionTokenType.selfString))) {
            return RSSelfExpression(this.previousToken)
        }

        else if (match(listOf(RSExpressionTokenType.explicitVariable))) {
            return this.explicitVariableExpressionValue()
        }

        else if (match(listOf(RSExpressionTokenType.implicitVariable))) {
            return this.implicitVariableExpressionValue()
        }

        else {
            throw RSExpressionParserError.invalidToken("Expected value, got ${this.currentToken}")
        }

    }

    @Throws(RSExpressionParserError::class)
    fun comparisonOperation(): RSExpressionToken {

        if (match(listOf(
                        RSExpressionTokenType.equal, RSExpressionTokenType.equalEqual,
                        RSExpressionTokenType.bangNotEqual, RSExpressionTokenType.angleNotEqual,
                        RSExpressionTokenType.gte, RSExpressionTokenType.egt, RSExpressionTokenType.gt,
                        RSExpressionTokenType.lte, RSExpressionTokenType.elt, RSExpressionTokenType.lt
                ))) {
            return this.previousToken
        }
        else {
            throw RSExpressionParserError.invalidToken("Expected comparison operation, got ${this.currentToken}")
        }

    }

    @Throws(RSExpressionParserError::class)
    fun explicitVariableExpressionValue(): RSExpression {

        var expr: RSExpression = RSExplicitVariableExpression(this.previousToken)
        while(match(listOf(RSExpressionTokenType.dot, RSExpressionTokenType.leftSquareBrace))) {

            when (this.previousToken.type) {

                RSExpressionTokenType.dot -> {

                    val op = this.previousToken
                    val right = this.consume(RSExpressionTokenType.implicitVariable)
                    expr = RSKeypathAccessExpression(expr, op, right)

                }

                RSExpressionTokenType.leftSquareBrace -> {

                    val op = this.previousToken
                    if (match(listOf(RSExpressionTokenType.firstString, RSExpressionTokenType.lastString, RSExpressionTokenType.sizeString))) {
                        val right = this.previousToken
                        expr = RSCollectionElementSpecialAccessExpression(expr, op, right)
                    }
                    else {
                        val right = this.expression()
                        expr = RSCollectionElementIndexedAccessExpression(expr, op, right)
                    }

                    this.consume(RSExpressionTokenType.rightSquareBrace)

                }

                else -> {
                    throw RSExpressionParserError.invalidToken("Expected dot or brace operator, got ${this.previousToken}")
                }

            }

        }

        return expr

    }

    @Throws(RSExpressionParserError::class)
    fun implicitVariableExpressionValue(): RSExpression {

        var expr: RSExpression = RSImplicitVariableExpression(this.previousToken)
        while(match(listOf(RSExpressionTokenType.dot, RSExpressionTokenType.leftSquareBrace))) {

            when (this.previousToken.type) {

                RSExpressionTokenType.dot -> {

                    val op = this.previousToken
                    val right = this.consume(RSExpressionTokenType.implicitVariable)
                    expr = RSKeypathAccessExpression(expr, op, right)

                }

                RSExpressionTokenType.leftSquareBrace -> {

                    val op = this.previousToken
                    if (match(listOf(RSExpressionTokenType.firstString, RSExpressionTokenType.lastString, RSExpressionTokenType.sizeString))) {
                        val right = this.previousToken
                        expr = RSCollectionElementSpecialAccessExpression(expr, op, right)
                    }
                    else {
                        val right = this.expression()
                        expr = RSCollectionElementIndexedAccessExpression(expr, op, right)
                    }

                    this.consume(RSExpressionTokenType.rightSquareBrace)

                }

                else -> {
                    throw RSExpressionParserError.invalidToken("Expected dot or brace operator, got ${this.previousToken}")
                }

            }

        }

        return expr

    }

    @Throws(RSExpressionParserError::class)
    fun stringPredicateExpression(): RSBooleanExpression {
        throw RSExpressionParserError.invalidToken("String Predicate Expressions are not yet supported")
    }

    @Throws(RSExpressionParserError::class)
    fun inCollectionPredicateExpression(): RSBooleanExpression {

        val left = expression()
        val op = inCollectionOperation()
        val right = expression()

        return RSInCollectionExpression(left, op, right)
    }

    @Throws(RSExpressionParserError::class)
    fun inCollectionOperation(): RSExpressionToken {

        if(match(listOf(RSExpressionTokenType.inString))) {
            return this.previousToken
        }
        else {
            throw RSExpressionParserError.invalidToken("Expected IN collection operation, got ${this.previousToken}")
        }


    }







}