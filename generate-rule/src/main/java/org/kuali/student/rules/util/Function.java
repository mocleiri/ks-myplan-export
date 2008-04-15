/**
 * 
 */
package org.kuali.student.rules.util;

import java.util.List;
import java.util.ArrayList;

import org.kuali.student.rules.runtime.ast.BinaryTree;
import org.kuali.student.rules.runtime.ast.BooleanNode;

/**
 * @author Rich Diaz
 *
 */

public class Function {
	private String functionString;
	private BinaryTree ASTtree;
	private BooleanNode root;
	private ArrayList<String> funcVars = new ArrayList<String>();
	private ArrayList<String> symbols = new ArrayList<String>();
	
	public static final void main(String[] args) {
		
		Function f = new Function("A0*B4+(C*D)");
		
		
		List<String> funcVars = f.getVariables();
		for (String var : funcVars) {
			System.out.println("The var is " + var );
        }
		
		List<String> symbols = f.getSymbols();
		for (String symbol : symbols) {
			System.out.println("The symbol is " + symbol );
        }
	}
	
	public Function(String functionString) {
		
		// Since the AST calls the Boolean Function parser, 
		// calling the AST here makes sure that the function is valid
		ASTtree = new BinaryTree();
		root = ASTtree.buildTree( functionString );
		
		this.functionString = functionString;
	}
	
	/** Parse function string to get the variables
	 * 1. Need this function at rule creation time (i.e when in BRMS) to insert the
	 * 		action part of the rules
	 * 2. Also need this at runtime to populate the HashMaps in the proposition
	 *		which are used as tree leaf nodes in the AST.
	 */
	public List<String> getVariables() {
		
		ASTtree.traverseTreePostOrderDontSetNode(root, null);
		List<BooleanNode> treeNodes = ASTtree.getAllNodes();
		
		for (BooleanNode bnode : treeNodes) {
			String label = bnode.getLabel();
			if (label.equals("+") || label.equals("*") ){
				continue;
        	}
	       	funcVars.add(label);
        }
	    return funcVars;
	}
	
	/** Parse function string to get all the symbols. Need this only at rule creation time.
	* 	Can't load the symbols in getVariables() because the AST removes the parenthesis and we need those.
	* 	Get them from the string which has been validated in the constructor Function()
	* 	Function example A0*B4+(C*D)
	*/
	public List<String> getSymbols() {
		
		for (int i=0; i < functionString.length(); i++){
			
			if (i+2 <= functionString.length()) {
				String aSubStr = functionString.substring(i, i+2);
				if (aSubStr.matches("[A-Z][0-9]")){
					symbols.add(functionString.substring(i, i+2) );
					i++;
				}else{
					symbols.add(functionString.substring(i, i+1) );
				}
			}else {
				symbols.add(functionString.substring(i, i+1) );
			}
		}
		return symbols;
	}
}
