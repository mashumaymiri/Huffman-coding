import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("1- encode file \n2- decode file");
		int opt = sc.nextInt();
		if (opt == 1) 
			System.out.println("Enter file name to encode: ");
		else if (opt == 2)
			System.out.println("Enter encoded file name to decode: ");
		else
			return;
		
		if (opt == 1) {
			String opt2 = sc.next();
			long startTime = System.currentTimeMillis();
			long StartTime = System.currentTimeMillis();
			String s = readfromfile(opt2);
			// encode
			MinHeap tree = HuffmanEncode(s);
			long endtime = System.currentTimeMillis();
			double time = endtime - startTime;
			System.out.println("Time to Encode : "+time+" ms");
			startTime = System.currentTimeMillis();
			// decode
			String decoded = HuffmanDecode(tree);
			endtime = System.currentTimeMillis();
			time = endtime - startTime;
			System.out.println("Time to Decode : "+time+" ms");
			startTime = System.currentTimeMillis();
			// save the encoded file
			EncodetoFile(tree, s);
			endtime = System.currentTimeMillis();
			time = endtime - startTime;
			System.out.println("Time to Encode to file : "+time+" ms");
			long endTime = System.currentTimeMillis();
			double timeElapsed = endTime - StartTime;
			System.out.println("Total time: "+timeElapsed+" ms");
		} else {
			String opt2 = sc.next();
			long startTime = System.currentTimeMillis();
			MinHeap heap = ReadEncodedFile(opt2);
			heap.CreateTreeMap();
			String decoded = HuffmanDecode(heap);
			long endTime = System.currentTimeMillis();
			double timeElapsed = endTime - startTime;
			System.out.println("Time Took : "+timeElapsed/1000000+" ms");
		}
	}

	public static void PrintEncodedInfo(MinHeap heap, String s, int NumberOfCharacters) {
		int encodedStringlength = heap.encodedStringlength;
		if (encodedStringlength < 1000)
			System.out.println("Encoded String : \n"+heap.getencodedstring());
		System.out.println("Bits for Encoded String : "+encodedStringlength);
		int tbfc = 0;
		for (int i=0;i<heap.csize;i++)
			if (heap.codeWords[i].length()> 0)
				tbfc += heap.codeWords[i].length()-2;
		System.out.println("Total bits for codewords : "+tbfc+" Bits");
		System.out.println("Characters : "+(NumberOfCharacters*8)+" Bits");
		System.out.println("total = "+encodedStringlength+" + "+tbfc+" + "+(NumberOfCharacters*8)+" = "+(encodedStringlength+tbfc+(NumberOfCharacters*8)));
	}
	// start the encoding process by counting the letters and inserting them to the heap
	public static MinHeap HuffmanEncode(String s) {
		Node[] ar = new Node[s.length()]; 
		int size = 0;
		// count the frequency of letters 
		for (int i=0;i<s.length();i++) {
			boolean found = false;
			for (int j=0;j<size;j++) {
				if (ar[j].c == s.charAt(i)) {
					ar[j].freq++;
					found = true;
					break;
				}
			}
			if (!found)
				ar[size++] = new Node(s.charAt(i), 1);
		}
		// create the heap and add the letters to it 
		MinHeap heap = new MinHeap(size);
		for (int j=0;j<size;j++) {
			heap.insert(ar[j]);
		}
		// build huffman tree
		MinHeap output = BuildHuffmanTree(heap, size);
		output.createcodeword(false);
		output.encodeString(s);
		PrintEncodedInfo(output, s, size);
		return output;
	}
	// build the huffman tree 
	public static MinHeap BuildHuffmanTree(MinHeap heap, int size) {
		// while the heap has more than one node
		while (heap.size > 1) {
			// create a new node
			Node temp = new Node();
			int t1 = 0, t2 = 0;
			// left side of the new node is the least frequent node in the heap
			temp.left = heap.remove();
			temp.left.root = temp;
			if (temp.left != null)
				t1 = temp.left.freq;
			// right side of the new node is the second least frequent node in the heap
			temp.right = heap.remove();
			temp.right.root = temp;
			if (temp.right != null)
				t2 = temp.right.freq;
			// make the frequency of the new node is the total node of the total 2 nodes
			temp.freq = t1 + t2;
			temp.c = '-';
			// insert the new node back into the heap
			heap.insert(temp);
		}
		// convert the huffman tree to dot and use graphviz to convert into png 
		try {
		      FileWriter myWriter = new FileWriter(System.getProperty("user.dir") + "\\src\\test.dot");
		      myWriter.write(heap.getroot().toDot());
		      myWriter.close();
		      Runtime.getRuntime().exec("\"C:\\Program Files\\Graphviz\\bin\\dot.exe\" -Tpng "+System.getProperty("user.dir") + "\\src\\test.dot -o "+System.getProperty("user.dir") + "\\src\\output.png");
			} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return heap;
	}
	public static String getstringtoencode(MinHeap a, String s) {
		String output = "";
		a.createcodeword(true);
		a.encodeString(s);
		output += a.getencodedstring()+"\n";
		for (int i=0;i<a.csize;i++) {
			if (a.codeWords[i].length()>0) {
				if (a.codeWords[i].charAt(0) == '\n')
					output += "Space:"+a.codeWords[i].substring(2)+"\n";
				else
					output += a.codeWords[i]+"\n";
			}
		}
		return output;
	}
	// save the encoded string and its tree to a string file
	public static void EncodetoFile(MinHeap a, String s) {
		try {
		      FileWriter myWriter = new FileWriter(System.getProperty("user.dir") + "\\src\\EncodedFile.txt");
		      myWriter.write(getstringtoencode(a, s));
		      myWriter.close();
			} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	public static void WriteOutputtofile(String s) {
		System.out.println("Decoded String was written to DecodedOutput.txt");
		try {
		      FileWriter myWriter = new FileWriter(System.getProperty("user.dir") + "\\src\\DecodedOutput.txt");
		      myWriter.write(s);
		      myWriter.close();
			} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	// decode the string using the codewords and calculate the bits for it 
	public static String HuffmanDecode(MinHeap a) {
		String output = "";
		for (int i=0;i<a.essize;i++) {
			output += a.decode(a.encodedstring[i]);
		}
		if (output.length() < 1000)
			System.out.println("Decoded String : \n"+output);
		else
			WriteOutputtofile(output);
		System.out.println("Bits for Decoded String :"+((output.length()*8) + (lines*8)));
		return output;
	}
	static int lines = 0;
	// read the file to encode later and return the string
	public static String readfromfile(String file) {
		String output = "";
			try {
			      File myObj = new File(System.getProperty("user.dir") + "\\src\\" + file+".txt");
			      Scanner myReader = new Scanner(myObj);
			      lines = 0;
			      while (myReader.hasNextLine()) {
			    	  // combine the lines into one string
			    	  output += myReader.nextLine();
			    	  // add \n for each new line
			    	  output += "\n";
			    	  lines++;
			      }
			      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return output;
	}
	// read an encoded file and its huffman tree
	public static MinHeap ReadEncodedFile(String file) {
		int lines = 0;
		MinHeap a = null;
		try {
			// count the lines of the file to assign the size of the heap
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\src\\" + file+".txt"));
			while (reader.readLine() != null) lines++;
			reader.close();
			// create the heap with its size as the lines of the file
			a = new MinHeap(lines);
		      File myObj = new File(System.getProperty("user.dir") + "\\src\\" + file+".txt");
		      Scanner myReader = new Scanner(myObj);
		      int bits = 0;
		      if (myReader.hasNextLine()) {
		    	  // read the first line which is the encoded string
		    	  String s = myReader.nextLine();
		    	  // read the second line which is the first node in the huffman tree
		    	  String s2 = myReader.nextLine();
		    	  // calculate the number of bits for each character from the second line
		    	  if (s2.substring(0, 5).equals("Space")) {
		    		  bits = s2.substring(2).length()-4;
		    		  a.Spacebits = s2.substring(6);
		    	  }
		    	  else
		    		  bits = s2.substring(2).length();
		    	  // add the second line to the codewords
		    	  a.codeWords[s2.charAt(0)] = s2;
		    	  // create the array for the encoded string with size (String length divided by number of bits for each character)
		    	  a.encodedstring = new String[s.length()/bits];
		    	  // loop through the string to add to the array 
		    	  for (int i=0;i<s.length();i=i+bits) {
		    		  a.encodedstring[a.essize++] = s.substring(i, i+bits);
		    	  }
		      }
		      // loop through the other lines for creating the codewords
		      while (myReader.hasNextLine()) {
		    	  String NL = myReader.nextLine();
		    	  a.codeWords[NL.charAt(0)] = NL;
		    	  //a.codeWords[NL.charAt(0)] = a.codeWords[NL.charAt(0)].substring(4);
		    	  if (a.codeWords[NL.charAt(0)].substring(0, 5).equals("Space")) 
		    		  a.Spacebits = a.codeWords[NL.charAt(0)].substring(6);
		      }
		      myReader.close();
	    } catch (Exception e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	return a;
	}
}
