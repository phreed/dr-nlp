## Stream of Consciousness

#### Recast textual specification as an annotated grammer (ANTLR 4).

#### Maven project

#### JUnit for tests

#### GitHub project


#### Write a lexer (tokenizer) and parser (tree builder).

 The grammar is assumed to be psuedo-english where the characters are limited to
 the latin characters and are case-insensitive.
 This is a limitation as not even English follows this rule.
 This can be corrected by introducing a new lexer.
 For this reason (quasi-)dependency-injection is used so that the lexer may be readily replaced.

 The parser works with integers as its tokens this cuts down on size
 and provides separation-of-concerns between the lexer and the parser (typical approach).
 A drawback is that the number of integers is finite but in this small problem we can ignore that.

 It is common for words in english to be hyphenated for a number of reasons.
 Rather than dealing with the details of the hyphenation problem I will
 treat hyphens as collapsing characters (as opposed to simple white-space).
 So, for example "white-space" will be treated identically to "whitespace".
 This implies that a hyphen should consume any surrounding whitespace as it
 can be used for word continuation.

 * Efficient storage.

 The lexer identifies each word adding it to a hash-map and generating an integer representation for the word.
 This is used by the lexer.

 There is also an array-list that maps the integer to the words for reverse lookup.
 Sentence-boundaries are detected by the lexer, the boundaries are indicated by:
   * a period followed by whitespace followed by an upper-case character
   * The first non-whitespace character in the stream
   * The last non-whitespace character in the stream

 These rules guarantee that every word will fall between sentence-boundaries and
 therefore be part of exactly one sentence.

 The AST then contains sentences which are composed of words.


#### Process AST

  * Count the named-entities [case-insensitive]




