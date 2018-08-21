package org.researchsuite.researchsuiteextensions.predicate

class RSExpressionTokenizer(val text: String) {

    var iterator: CharIterator
    var pushedBackCharacter: Char? = null

    init {
        this.iterator = text.iterator()
    }

    companion object {

        @Throws(RSExpressionTokenizerError::class)
        fun generateTokens(text: String): List<RSExpressionToken> {

            val tokenizer = RSExpressionTokenizer(text)
            var tokens = listOf<RSExpressionToken>()

            while (true) {
                val token = tokenizer.nextToken()
                if (token != null) {
                    tokens = tokens + token
                }
                else {
                    break
                }
            }

            tokens = tokens + RSExpressionToken(RSExpressionTokenType.eof, null)

            return tokens
        }

        fun isWordCharacter(ch: Char): Boolean {
            return ch.isLetterOrDigit() || ch == '_'
        }
    }

    @Throws(RSExpressionTokenizerError::class)
    fun nextToken(): RSExpressionToken? {

        while (true) {

            val ch = nextCharacter()
            if (ch != null) {

                when (ch) {

                    ' ', '\n', '\r', '\t' -> {
                        //ignore whitespace
                        //do nothing
                    }

                    '%' -> {
                        return RSExpressionToken(RSExpressionTokenType.percent, null)
                    }

                    '@' -> {
                        return RSExpressionToken(RSExpressionTokenType.at, null)
                    }

                    '(' -> {
                        return RSExpressionToken(RSExpressionTokenType.leftParen, null)
                    }

                    ')' -> {
                        return RSExpressionToken(RSExpressionTokenType.rightParen, null)
                    }

                    '[' -> {
                        return RSExpressionToken(RSExpressionTokenType.leftSquareBrace, null)
                    }

                    ']' -> {
                        return RSExpressionToken(RSExpressionTokenType.rightSquareBrace, null)
                    }

                    '{' -> {
                        return RSExpressionToken(RSExpressionTokenType.leftCurlyBrace, null)
                    }

                    '}' -> {
                        return RSExpressionToken(RSExpressionTokenType.rightCurlyBrace, null)
                    }

                    ',' -> {
                        return RSExpressionToken(RSExpressionTokenType.comma, null)
                    }

                    '.' -> {
                        return RSExpressionToken(RSExpressionTokenType.dot, null)
                    }

                    '=' -> {
                        return equalsToken()
                    }

                    '<' -> {
                        return lessThanToken()
                    }

                    '>' -> {
                        return greaterThanToken()
                    }

                    '!' -> {
                        return bangToken()
                    }

                    '&' -> {
                        return ampToken()
                    }

                    '|' -> {
                        return pipeToken()
                    }

                    '$' -> {
                        return explicitVariableToken()
                    }

                    in '0'..'9', '-' -> {
                        return integerToken(ch)
                    }

                    '\"' -> {
                        return stringToken()
                    }

                    in 'a'..'z', in 'A'..'Z' -> {
                        return reservedStringToken(ch)
                    }

                    else -> {
                        throw RSExpressionTokenizerError.unrecognizedCharacter(ch)
                    }

                }

            }
            else {
                break
            }
        }

        return null

    }

    fun nextCharacter(): Char? {

        val next = this.pushedBackCharacter ?: (if (this.iterator.hasNext()) this.iterator.nextChar() else null)
        this.pushedBackCharacter = null
        return next

    }

    @Throws(RSExpressionTokenizerError::class)
    fun equalsToken(): RSExpressionToken {

        val ch = nextCharacter()
        when (ch) {

            ' ', '\n', '\r', '\t' -> {
                return RSExpressionToken(RSExpressionTokenType.equal, null)
            }

            '=' -> {
                return RSExpressionToken(RSExpressionTokenType.equalEqual, null)
            }

            '>' -> {
                return RSExpressionToken(RSExpressionTokenType.egt, null)
            }

            '<' -> {
                return RSExpressionToken(RSExpressionTokenType.elt, null)
            }

            else -> {
                throw RSExpressionTokenizerError.unexpectedCharacter(ch!!)
            }

        }

    }

    @Throws(RSExpressionTokenizerError::class)
    fun lessThanToken(): RSExpressionToken {

        val ch = nextCharacter()
        when (ch) {

            ' ', '\n', '\r', '\t' -> {
                return RSExpressionToken(RSExpressionTokenType.lt, null)
            }

            '=' -> {
                return RSExpressionToken(RSExpressionTokenType.lte, null)
            }

            '>' -> {
                return RSExpressionToken(RSExpressionTokenType.angleNotEqual, null)
            }

            else -> {
                throw RSExpressionTokenizerError.unexpectedCharacter(ch!!)
            }

        }

    }

    @Throws(RSExpressionTokenizerError::class)
    fun greaterThanToken(): RSExpressionToken {

        val ch = nextCharacter()
        when (ch) {

            ' ', '\n', '\r', '\t' -> {
                return RSExpressionToken(RSExpressionTokenType.gt, null)
            }

            '=' -> {
                return RSExpressionToken(RSExpressionTokenType.gte, null)
            }

            else -> {
                throw RSExpressionTokenizerError.unexpectedCharacter(ch!!)
            }

        }

    }

    @Throws(RSExpressionTokenizerError::class)
    fun bangToken(): RSExpressionToken {

        val ch = nextCharacter()
        when (ch) {

            ' ', '\n', '\r', '\t' -> {
                return RSExpressionToken(RSExpressionTokenType.bang, null)
            }

            '=' -> {
                return RSExpressionToken(RSExpressionTokenType.bangNotEqual, null)
            }

            else -> {
                throw RSExpressionTokenizerError.unexpectedCharacter(ch!!)
            }

        }

    }

    @Throws(RSExpressionTokenizerError::class)
    fun ampToken(): RSExpressionToken {

        val ch = nextCharacter()
        when (ch) {

            '&' -> {
                return RSExpressionToken(RSExpressionTokenType.doubleAmp, null)
            }

            else -> {
                throw RSExpressionTokenizerError.unexpectedCharacter(ch!!)
            }

        }

    }

    @Throws(RSExpressionTokenizerError::class)
    fun pipeToken(): RSExpressionToken {

        val ch = nextCharacter()
        when (ch) {

            '|' -> {
                return RSExpressionToken(RSExpressionTokenType.doublePipe, null)
            }

            else -> {
                throw RSExpressionTokenizerError.unexpectedCharacter(ch!!)
            }

        }

    }

    @Throws(RSExpressionTokenizerError::class)
    fun explicitVariableToken(): RSExpressionToken {

        var tokenText = String()
        var found = false

        while (true) {

            val ch = nextCharacter()
            if (ch != null) {

                when (ch) {
                    ' ', '\n', '\r', '\t' -> {
                        return RSExpressionToken(RSExpressionTokenType.explicitVariable, tokenText)
                    }
                    else -> {
                        if (RSExpressionTokenizer.isWordCharacter(ch)) {
                            tokenText = tokenText + ch
                        }
                        else {
                            //not whitespace or word character, return
                            found = true
                            this.pushedBackCharacter = ch
                        }
                    }
                }

                if (found) {
                    break
                }

            } else {
                break
            }
        }

        if (tokenText.count() > 0) {
            return RSExpressionToken(RSExpressionTokenType.explicitVariable, tokenText)
        }
        else {
            throw RSExpressionTokenizerError.unterminatedString()
        }

    }

    @Throws(RSExpressionTokenizerError::class)
    fun integerToken(first: Char): RSExpressionToken {

        var tokenText: String = first.toString()
        var found = false

        while (true) {

            val ch = nextCharacter()
            if (ch != null) {

                when (ch) {

                    in '0'..'9' -> {
                        tokenText = tokenText + ch
                    }

                    else -> {
                        this.pushedBackCharacter = ch
                        found = true
                    }
                }

                if (found) {
                    break
                }

            } else {
                break
            }
        }

        val value = tokenText.toIntOrNull()
        if (value != null) {
            return RSExpressionToken(RSExpressionTokenType.intLiteral, value)
        }
        else {
            throw RSExpressionTokenizerError.malformedInteger()
        }

    }

    @Throws(RSExpressionTokenizerError::class)
    fun stringToken(): RSExpressionToken {

        var tokenText: String = String()
        var terminated = false

        while (true) {

            val ch = nextCharacter()
            if (ch != null) {

                when (ch) {

                    '\"' -> {
                        terminated = true
                    }

                    else -> {
                        tokenText = tokenText + ch
                    }
                }

                if (terminated) {
                    break
                }

            } else {
                break
            }
        }

        if (!terminated) {
            throw RSExpressionTokenizerError.unterminatedString()
        }
        else {
            return RSExpressionToken(RSExpressionTokenType.stringLiteral, tokenText)
        }

    }

    @Throws(RSExpressionTokenizerError::class)
    fun reservedStringToken(first: Char): RSExpressionToken {

        var tokenText: String = first.toString()
        var found = false

        while (true) {

            val ch = nextCharacter()
            if (ch != null) {

                when (ch) {

                    ' ', '\n', '\r', '\t' -> {
                        found = true
                    }

                    else -> {

                        if (RSExpressionTokenizer.isWordCharacter(ch)) {
                            tokenText = tokenText + ch
                        }
                        else {
                            this.pushedBackCharacter = ch
                            found = true
                        }

                    }
                }

                if (found) {
                    break
                }

            } else {
                break
            }
        }

        when (tokenText.toLowerCase()) {

            "truepredicate" -> {
                return RSExpressionToken(RSExpressionTokenType.truepredicate, null)
            }

            "falsepredicate" -> {
                return RSExpressionToken(RSExpressionTokenType.falsepredicate, null)
            }

            "and" -> {
                return RSExpressionToken(RSExpressionTokenType.andString, null)
            }

            "or" -> {
                return RSExpressionToken(RSExpressionTokenType.orString, null)
            }

            "not" -> {
                return RSExpressionToken(RSExpressionTokenType.notString, null)
            }

            "false" -> {
                return RSExpressionToken(RSExpressionTokenType.falseString, null)
            }

            "true" -> {
                return RSExpressionToken(RSExpressionTokenType.trueString, null)
            }

            "null" -> {
                return RSExpressionToken(RSExpressionTokenType.nullString, null)
            }

            "nil" -> {
                return RSExpressionToken(RSExpressionTokenType.nilString, null)
            }

            "self" -> {
                return RSExpressionToken(RSExpressionTokenType.selfString, null)
            }

            "first" -> {
                return RSExpressionToken(RSExpressionTokenType.firstString, null)
            }

            "last" -> {
                return RSExpressionToken(RSExpressionTokenType.lastString, null)
            }

            "size" -> {
                return RSExpressionToken(RSExpressionTokenType.sizeString, null)
            }

            "any" -> {
                return RSExpressionToken(RSExpressionTokenType.anyString, null)
            }

            "some" -> {
                return RSExpressionToken(RSExpressionTokenType.someString, null)
            }

            "all" -> {
                return RSExpressionToken(RSExpressionTokenType.allString, null)
            }

            "none" -> {
                return RSExpressionToken(RSExpressionTokenType.noneString, null)
            }

            "in" -> {
                return RSExpressionToken(RSExpressionTokenType.inString, null)
            }

            "beginswith" -> {
                return RSExpressionToken(RSExpressionTokenType.beginswith, null)
            }

            "contains" -> {
                return RSExpressionToken(RSExpressionTokenType.contains, null)
            }

            "endswith" -> {
                return RSExpressionToken(RSExpressionTokenType.endswith, null)
            }

            "like" -> {
                return RSExpressionToken(RSExpressionTokenType.like, null)
            }

            "matches" -> {
                return RSExpressionToken(RSExpressionTokenType.matches, null)
            }

            else -> {
                return RSExpressionToken(RSExpressionTokenType.implicitVariable, tokenText)
            }

        }

    }

}