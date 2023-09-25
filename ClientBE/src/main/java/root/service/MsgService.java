package root.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import root.model.HNode;

public class MsgService {
	
	public byte[] compress(String msg) {
		System.out.println("\ncompressing: "+msg);
		build(msg);
		return (msg+"_compressed").getBytes();
	}
	
	public byte[] compress2(String msg) {
		System.out.println("comp "+msg);
		String encodedStr = "";
		if(msg!=null && msg.length()!=0) {
			HNode root = build2(msg);
			System.out.println(root);
			Map<Character, String> huffmanCode = new HashMap<>();
			encode(root, "", huffmanCode);
			System.out.println("--> Huffman Codes: " + huffmanCode);
			for(char c: msg.toCharArray()) {
				encodedStr += huffmanCode.get(c);
			}
			System.out.println("ans====: "+encodedStr);
		}
		//return (msg+"_compressed").getBytes();
		
		return encodedStr.getBytes();
	}
	//010111011110011001000011
	//010111011110011001000011

	public void encode(HNode root, String str, Map<Character, String> hMap) {
		if(root==null)
			return;

		if (root.isLeaf())
			hMap.put(root.getCharac(), str.length() > 0 ? str : "1");

		encode(root.getLeft(), str + '0', hMap); //
		encode(root.getRight(), str + '1', hMap);
	}

	public int decode(HNode root, int index, StringBuilder sb) {
		if (root == null) 
			return index;

		if (root.isLeaf())	{
			System.out.print(root.getCharac());
			return index;
		}
		index++;
		root = (sb.charAt(index)=='0')?root.getLeft():root.getRight();
		index = decode(root, index, sb);
		
		return index;
	}

	public void build(String text) {
		if(text == null || text.length() == 0) 
			return;

		Map<Character, Integer> freqMap = new HashMap<>();
		for(char c: text.toCharArray()) {
			freqMap.put(c, freqMap.getOrDefault(c,0)+1);
		}

		PriorityQueue<HNode> pQ;
		pQ = new PriorityQueue<>(Comparator.comparingInt(x -> x.getFrequency()));

		for(var entry: freqMap.entrySet()) {
			pQ.add(new HNode(entry.getKey(), entry.getValue()));
		}

		while(pQ.size() != 1) {
			HNode left = pQ.poll();
			HNode right = pQ.poll();

			int sum = left.getFrequency() + right.getFrequency();
			pQ.add(new HNode(null, sum, left, right));
		}

		HNode root = pQ.peek();
		Map<Character, String> huffmanCode = new HashMap<>();
		encode(root, "", huffmanCode);

		//System.out.println("The Huffman Codes forthe given text are: " + huffmanCode);
		System.out.println("The original text size: " + text.length()+" bytes");

		StringBuilder encodedStr = new StringBuilder();
		for(char c: text.toCharArray()) {
			encodedStr.append(huffmanCode.get(c));
		}

		System.out.println("The encoded text size: " + encodedStr.length()/8);
		//System.out.print("The decoded text is: ");

		if(root.isLeaf()) {
			while(root.getFrequency()-1 > 0) {
				System.out.print(root.getCharac());
			}
		}
		else {
			int index = -1;
			while(index<(encodedStr.length()-1)) {
				index = decode(root, index, encodedStr);
			}
		}
	}
	
	public HNode build2(String text) {
		if(text == null || text.length() == 0) 
			return null;

		Map<Character, Integer> freqMap = new HashMap<>();
		for(char c: text.toCharArray()) {
			freqMap.put(c, freqMap.getOrDefault(c,0)+1);
		}

		PriorityQueue<HNode> pQ;
		pQ = new PriorityQueue<>(Comparator.comparingInt(x -> x.getFrequency()));

		for(var entry: freqMap.entrySet()) {
			pQ.add(new HNode(entry.getKey(), entry.getValue()));
		}

		while(pQ.size() != 1) {
			HNode left = pQ.poll();
			HNode right = pQ.poll();

			int sum = left.getFrequency() + right.getFrequency();
			pQ.add(new HNode(null, sum, left, right));
		}

		HNode root = pQ.peek();
		
		return root;
	}
	
}
