package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class connectedComponent {
	private int numVertices;
	private int numEdges;
	private HashSet<String> total_vertices;
	private int[][] adjMartrix;
//	private HashMap<Integer,Double> finalResult;
	private String file_path;
	private HashMap<String, Integer> vertex_num;
	private HashMap<Integer, String> num_vertex;
	private HashMap<Integer,Integer> color;
	private Stack<Integer> myStack;
	private int flag;
	private HashMap<Integer,ArrayList<String>> result;
	
	public connectedComponent(String path) {
		numVertices = 0;
		numEdges = 0;
		total_vertices = new HashSet<String>();
		adjMartrix = new int[1000][1000];
//		finalResult = new HashMap<Integer,Double>();
		file_path = path;
		vertex_num = new HashMap<String, Integer>();
		num_vertex = new HashMap<Integer, String>();
		color = new HashMap<Integer,Integer>();
		myStack = new Stack<Integer>();
		flag = 1;
		result = new HashMap<Integer,ArrayList<String>>();
	}
	
	public connectedComponent() {
		this("output.txt");
	}
	
	private void readData() {
		BufferedReader reader = null;
		File file = new File(file_path);
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			String[] wordsArray;
			int value = 0;
			
			while((line = reader.readLine()) != null) {
				wordsArray = line.split("\\W+");
				String left_node = wordsArray[0];
				String right_node = wordsArray[1];
				numEdges++;				
				total_vertices.add(left_node);
				total_vertices.add(right_node);
				
				if(!vertex_num.containsKey(left_node)) {
					vertex_num.put(left_node, value);
					value++;
				}
				
				if(!vertex_num.containsKey(right_node)) {
					vertex_num.put(right_node, value);
					value++;
				}
				
				adjMartrix[vertex_num.get(right_node)][vertex_num.get(left_node)] = 1;
				adjMartrix[vertex_num.get(left_node)][vertex_num.get(right_node)] = 1;
			}
			
			
			for(String s: vertex_num.keySet()) {
				int result = vertex_num.get(s);
				num_vertex.put(result, s);
			}
			
			numVertices = total_vertices.size();
			System.out.println("total vertices is: " + numVertices);
			System.out.println("total edges is: " + numEdges);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void findComponent() {
		for(Integer s: vertex_num.values()) {
			color.put(s, 0);
		}
		for(Integer s: color.keySet()) {
			if(color.get(s) == 0) {
				dfs(s);
			}
		}
	}
	
	private void dfs(int u) {
		ArrayList<String> al = new ArrayList<String>();
		color.put(u, 1);
		al.add(num_vertex.get(u));
		myStack.push(u);
		while(!myStack.isEmpty()) {
			int cur = myStack.pop();
			for(int i=1; i <= numVertices; i++) {
				if(adjMartrix[i][cur] == 1 || adjMartrix[cur][i] == 1) {
					if(color.get(i) == 0) {
						color.put(i, 1);
						al.add(num_vertex.get(i));
						myStack.push(i);
					}					
				}
			}			
		}
//		Collections.sort(al);
		result.put(flag, al);
		flag++;
	}
	
	public void printResult() {
		for(Integer s: result.keySet()) {
			ArrayList<String> al = result.get(s);
			System.out.println("Component "+ s + ": " + al);
		}
	}
	
	
	public void testData() {
		System.out.println("total vertices is: " + numVertices);
		System.out.println("total edges is: " + numEdges);
		
		for(String a : vertex_num.keySet()) {
			System.out.println(a + "\t" + vertex_num.get(a));
		}
	}
	
	public static void main(String args[]) {
		connectedComponent cc = new connectedComponent(args[0]);
		cc.readData();
//		cc.testData();
		cc.findComponent();
		cc.printResult();
		
	}
}
