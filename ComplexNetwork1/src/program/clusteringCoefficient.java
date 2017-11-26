package project;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class clusteringCoefficient {
	private int numVertices;
	private int numEdges;
	private HashSet<String> total_vertices;
	private int[][] adjMartrix;
	private HashMap<String,Double> finalResult;
	private String file_path;
	private HashMap<String, Integer> vertex_num;
	private HashMap<Integer, String> num_vertex;
	private double sum_Result;
	
	public clusteringCoefficient(String path) {
		numVertices = 0;
		numEdges = 0;
		total_vertices = new HashSet<String>();
		adjMartrix = new int[1000][1000];
		finalResult = new HashMap<String,Double>();
		file_path = path;
		vertex_num = new HashMap<String, Integer>();
		num_vertex = new HashMap<Integer, String>();
		sum_Result = 0.0;
	}
	
	public clusteringCoefficient() {
		this("ttest.txt");
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
	
	public void calculateCC() {
		for(Integer a : vertex_num.values()) {
			core(a);
		}
	}
	
	public void core(int a) {
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		double cc_value = 0.0;
		
		
		
		for(int i = 0; i < numVertices; i++) {
			if(adjMartrix[a][i] == 1) {
				neighbours.add(i);
			}
		}
		
		
		int num_neighbours = neighbours.size();
		int num_connected = 0;
		for(int i = 0; i < num_neighbours-1; i++ ) {
			for(int j = i + 1; j < num_neighbours; j++) {
				if(adjMartrix[neighbours.get(i)][neighbours.get(j)] == 1) {
					num_connected++;
				}
			}
		}
		if(num_neighbours == 1 || num_neighbours == 0) {
			cc_value = 0.0;
		} else {
			cc_value = 2.0 * num_connected / (num_neighbours * (num_neighbours - 1));
		}
		
		finalResult.put(num_vertex.get(a), cc_value);
		sum_Result = sum_Result + cc_value;
//		System.out.println(num_vertex.get(a) + ": " + cc_value);
		
		
		
		// just for test arraylist neighbours store the currect result.
//		System.out.print(num_vertex.get(a) + ": ");
//		for(Integer s : neighbours) {
//			System.out.print(s + " ");
//		}
//		System.out.println();
	}
	
	public void testData() {
		System.out.println("total vertices is: " + numVertices);
		System.out.println("total edges is: " + numEdges);
		
		for(String a : vertex_num.keySet()) {
			System.out.println(a + "\t" + vertex_num.get(a));
		}
	}
	
	public void printResult() {
		double average = (double)sum_Result / numVertices;
		System.out.println("the average clustering coefficient is: " + average);
		for(String a : finalResult.keySet()) {
			System.out.println(a + ": " + finalResult.get(a));
		}
	}
	
	
	public static void main(String args[]) {
		clusteringCoefficient cc = new clusteringCoefficient(args[0]);
		cc.readData();
//		cc.testData();
		cc.calculateCC();
		cc.printResult();		
	} 
	
}
