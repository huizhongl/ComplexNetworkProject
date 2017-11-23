package project;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class calculate_degree {
	private int numVertices;
	private int numEdges;
	private HashSet<String> total_vertices;
	private int[][] adjMartrix;
	private HashMap<String,Integer> finalResult;
	private String file_path;
	private HashMap<String, Integer> vertex_num;
	private HashMap<Integer, String> num_vertex;
	
	public calculate_degree(String path) {
		numVertices = 0;
		numEdges = 0;
		total_vertices = new HashSet<String>();
		adjMartrix = new int[1000][1000];
		finalResult = new HashMap<String,Integer>();
		file_path = path;
		vertex_num = new HashMap<String, Integer>();
		num_vertex = new HashMap<Integer, String>();
	}
	
	public calculate_degree() {
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
	
	public void degree() {
		for(Integer a : vertex_num.values()) {
			core(a);
		}
	}
	
	private void core(int s) {
		int result = 0;
		for(int i = 0; i < numVertices; i++) {
			if(adjMartrix[s][i] == 1 || adjMartrix[i][s] == 1) {
				result++;
			}
		}
		finalResult.put(num_vertex.get(s), result);
	}
	
	public void printResult() {
		for(String a: finalResult.keySet()) {
			System.out.println("[" + a + "]" + "\t" + finalResult.get(a));
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
		calculate_degree cd = new calculate_degree(args[0]);
		cd.readData();
		cd.degree();
		cd.printResult();
	} 	
}

