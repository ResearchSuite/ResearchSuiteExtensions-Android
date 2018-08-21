package org.researchsuite.researchsuiteextensions.predicate


enum class RSExpressionTokenType {
    eof,

    //    special characters
    percent,
    at,
    leftParen,
    rightParen,
    leftSquareBrace,
    rightSquareBrace,
    leftCurlyBrace,
    rightCurlyBrace,
    comma,
    dot,

    //    Basic Comparisons
    equal,
    equalEqual,
    gte,
    egt,
    lte,
    elt,
    gt,
    lt,
    bangNotEqual,
    angleNotEqual,

    between,

    truepredicate,
    falsepredicate,

    andString,
    doubleAmp,
    orString,
    doublePipe,


    notString,
    bang,

    beginswith,
    contains,
    endswith,
    like,
    matches,
    utiConformsTo,
    utiEquals,

    anyString,
    someString,
    allString,
    noneString,
    inString,

    firstString,
    lastString,
    sizeString,

    falseString,
    trueString,
    noString,
    yesString,
    nullString,
    nilString,
    selfString,

    stringLiteral,
    intLiteral,

    explicitVariable,
    implicitVariable

}

data class RSExpressionToken(val type: RSExpressionTokenType, val value: Any?) {

}