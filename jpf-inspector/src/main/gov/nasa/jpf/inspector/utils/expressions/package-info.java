/**
 * The classes in this package represent user-typed expressions in the expression syntax.
 *
 * These classes are created from the ANTLR grammar. They store their data as strings but they have the capability
 * to be compared to actual ClassInfo/MethodInfo etc. instances. This comparison is string-wise, but the expressions
 * may have wildcards which is taken into account by the classes (they use regular expressions).
 *
 * The comparisons are cached so that we don't do too much regular expression parsing.
 *
 * See the documentation for what wildcards are allowed.
 */
package gov.nasa.jpf.inspector.utils.expressions;