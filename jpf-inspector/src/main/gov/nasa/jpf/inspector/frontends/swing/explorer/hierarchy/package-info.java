/**
 * Contains classes that represent nodes of the program state hierarchy.
 *
 * I could try to be funny and call this "hierarchy 4" but it's not really funny that I felt the need to add either
 * another system of classes that represent absolutely the same thing. I guess the DRY principle is crying somewhere
 * in a corner.
 *
 * To be fair, we did need these nodes to inherit from {@link javax.swing.tree.TreeNode} _and_ a representation based
 * on object type rather than its relationship to the parent was more helpful in this case because we need indefinite
 * recursion.
 */
package gov.nasa.jpf.inspector.frontends.swing.explorer.hierarchy;