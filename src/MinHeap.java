import java.util.TreeMap;

public class MinHeap {
    private Node[] Heap;
    public int size;
    private int maxsize;
    public String[] codeWords;
    public int csize = 0;
    public String[] encodedstring;
    public int essize = 0;
    public String Spacebits = "";
	public int encodedStringlength;
	public String lastencodedstring;
	TreeMap<String, String> Tmap = new TreeMap<String, String>();


    public MinHeap(int maxsize)
    {
        this.maxsize = maxsize;
        this.size = 0;
        // initializes the array of codewords to size 128 (number of ASCII characters)
        this.codeWords = new String[128];
        csize = 128;
        for (int i=0;i<csize;i++) 
    		codeWords[i] = "";
        // creates an array of nodes and fills the array with empty nodes
        Heap = new Node[this.maxsize + 1];
        for (int i=0;i<maxsize;i++)
        	Heap[i] = new Node();
    }
    // swap two nodes 
    private void swap(int pos1, int pos2)
    {
        Node tmp = Heap[pos1];
        Heap[pos1] = Heap[pos2];
        Heap[pos2] = tmp;
    }
    // heapify the array of nodes
    private void minHeapify(int pos)
    {
    	// if pos is out of the array or if its a leaf
    	if (pos > maxsize || (2 * pos) > maxsize || ((2 * pos) + 1) > maxsize)
    		return;
    	// if the node is not a leaf and its bigger than its child
    	if (!(pos > (size / 2) && pos <= size)) {
            if (Heap[pos].freq > Heap[2 * pos].freq || Heap[pos].freq > Heap[((2 * pos) + 1)].freq) {
                // swap it with its left child and min heapify the left child
            	if (Heap[2 * pos].freq < Heap[((2 * pos) + 1)].freq) {
                    swap(pos, 2 * pos);
                    minHeapify(2 * pos);
                    // swap it with its right child and min heapify the right child
                } else {
                    swap(pos, ((2 * pos) + 1));
                    minHeapify(((2 * pos) + 1));
                }
            }
        }
    }
    // insert a node into the heap 
    public void insert(Node ar)
    {
        if (size >= maxsize) {
            return;
        }
        // add the node to the bottom of the heap
        Heap[++size] = ar;
        int current = size;
        // while the node is bigger than its parent, swap it 
        while (Heap[current].freq < Heap[current / 2].freq) {
            swap(current, current / 2);
            current = current / 2;
        }
    }
    // remove a node and heapify the heap
    public Node remove()
    {
        Node popped = Heap[1];
        Heap[1] = Heap[size--];
        minHeapify(1);
        return popped;
    }
    // get the root of the heap
    public Node getroot() {
    	return Heap[size];
    }
    // creates the codeword for the heap tree
    public String[] createcodeword(boolean out) {
        // initializes the array of codewords to size 128 (number of ASCII characters)
    	codeWords = new String[128];
    	csize = 128;
    	for (int i=0;i<csize;i++) 
    		codeWords[i] = "";
    	// generate the codewords and get the depth of the tree
    	int depth = generatecodeword(Heap[size], "", 0);
    	// if the tree will be used for output to a file complete all codewords
    	if (out) 
	    	for (int i=0;i<csize;i++) 
				if (codeWords[i].length()>0) 
		    		if ((codeWords[i].length()-2) != depth) {
		    			for (int j=codeWords[i].length();j-2<depth;j++) {
		    				codeWords[i] += "0";
		    			}
		    		}
    	// add the codewords to a tree map
    	CreateTreeMap();		
    	return codeWords;
    }
    public void CreateTreeMap() {
    	for (int i=0;i<csize;i++) 
			if (codeWords[i].length() > 0) 
				Tmap.put(codeWords[i].substring(2), codeWords[i].substring(0, 1));	
    }
    // generates the codewords
    public int generatecodeword(Node root, String s, int depth) {
    	// if its a leaf return the character and the depth
        if (root.left == null && root.right == null) {
            //System.out.println(root.c + ":" + s);
            codeWords[root.c] = root.c + ":" + s;
            return depth;
        }
        // else go left and add a 0
        int t1 = generatecodeword(root.left, s + "0", depth+1);
        // and go right and add a 1
        int t2 = generatecodeword(root.right, s + "1", depth+1);
        // get the biggest depth 
        if (t2 > t1)
        	t1 = t2;
        // return the depth
        return t1;
    }
    // encode a given string
    public void encodeString(String s) {
        encodedstring = new String[s.length()];
        essize = 0;
        encodedStringlength = 0;
        // for each character in the string replace with out codewords
    	for (int i=0;i<s.length();i++) {
    		// the ASCII code of the character is its index in the codewords array
			encodedstring[essize++] = codeWords[s.charAt(i)].substring(2); // O(1) retrieving 
			encodedStringlength += encodedstring[essize-1].length();
    	}
    }
    //decode the given bits into a character using the codewords
	public char decode(String es) {
		char output = 0;
		if (es.equalsIgnoreCase(Spacebits))
			return '\n';		
		// use the tree map to search for the character using the bits as the key
		output = Tmap.get(es).charAt(0);
		return output;
	}
	// return the encoded string 
	public String getencodedstring() {
		String output = "";
		for (int i=0;i<essize;i++)
			output += encodedstring[i];
		return output;
	}
}