public class Node {
	public Node left;
	public Node right;
	public Node root;
	public char c;
	public int freq;

	
	public Node() {
		left = null;
		right = null;
		this.freq = 0;
	}
	
	public Node(char c, int freq) {
		this.c = c;
		this.freq = freq;
		left = null;
		right = null;
	}
	
	// used to translate the binary tree to .dot file so we can use it with graphviz
	static int k=0;
	String toDot(int rootLabel) {
		String s="";String dummy="null";
		String roo= dummy + rootLabel;
		if (!(left== null)) {
			s += dummy + (k+1) + " [ label=\""+(left.c+":"+left.freq)+"\"];\n";
			s=s+roo + " -> " + dummy + (k+1) + ";\n";
			k=k+2;
			s=s+left.toDot(k-1);
		} 
		if (!(right== null)) {
			s+= dummy + (k+1) +" [ label=\""+(right.c+":"+right.freq)+"\"];\n";
			s=s+roo+" -> "+dummy+(k+1)+";\n";
			k=k+2;
			s=s+right.toDot(k-1);
		} 
		return s;
	}
	
	String toDot() {
		String s="digraph BST {\n";
		s+="null0 [label=\""+c+":"+freq+"\"];\n";
		s+=toDot(0);
		return s+"}";
	}
}
