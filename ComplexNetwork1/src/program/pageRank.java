package project;

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class pageRank {
	private int numVertices;
	private int numEdges;
	private HashSet<String> total_vertices;
	private int[][] adjMartrix;
	private String file_path;
	private HashMap<String, Integer> vertex_num;
	private HashMap<Integer, String> num_vertex;
	private HashMap<Integer, Double> pre_pr;
	private HashMap<Integer, Double> new_pr;
	private HashMap<String, Double> result;
	private Double alpha; 
	private Double beta;
	
	public pageRank() {
		this("ttest.txt");
//		numVertices = 0;
//		numEdges = 0;
//		total_vertices = new HashSet<Integer>();
//		adjMartrix = new int[5000][5000];
//		file_path = "ttest.txt";
//		vertex_num = new HashMap<Integer, Integer>();
//		num_vertex = new HashMap<Integer, Integer>();
//		pre_pr = new HashMap<Integer, Double>();
//		new_pr = new HashMap<Integer, Double>();
//		result = new HashMap<Integer, Double>();
//		alpha = 0.7;
//		beta = 0.1;		
	}
	
	public pageRank(String a) {
		numVertices = 0;
		numEdges = 0;
		total_vertices = new HashSet<String>();
		adjMartrix = new int[5000][5000];
		file_path = a;
		vertex_num = new HashMap<String, Integer>();
		num_vertex = new HashMap<Integer, String>();
		pre_pr = new HashMap<Integer, Double>();
		new_pr = new HashMap<Integer, Double>();
		result = new HashMap<String, Double>();
		alpha = 0.7;
		beta = 0.1;
		
	}
	
	public void readData() {
		BufferedReader reader = null;
		File file = new File(file_path);
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			String[] wordsArray;
			int value = 1;
			
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
			numVertices = total_vertices.size();
			System.out.println("total vertices is: " + numVertices);
			System.out.println("total edges is: " + numEdges);
//			System.out.println("It takes 5 min to calculate page rank value of the graph which has 1k vertex and 250k edges. Pls be patient and drink a coffee. Thank you.");
			System.out.println();
			
			for(String s: vertex_num.keySet()) {
				int result = vertex_num.get(s);
				num_vertex.put(result, s);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	public void initial() {
		for(Integer s : num_vertex.keySet()) {
			pre_pr.put(s, 1.0);
			new_pr.put(s, 1.0);
		}
		

	}
	
	public void secret() {
		
//		for(Integer s: pre_pr.keySet()) {
//			new_pr.put(s, pre_pr.get(s));
//		}
		
		for(Integer a : new_pr.keySet()) {
			Double pr_s = 0.0;
			Double value_s = 0.0;
			ArrayList<Integer> neighbours = find_neighbours(a);
			for(Integer b : neighbours) {
				Double pr_b = new_pr.get(b);
				int out_degree = find_outDegree(b);
				value_s = value_s + pr_b/out_degree;
			}
			pr_s = beta + alpha * value_s;
			new_pr.put(a, pr_s);
		}
	}
	
	public void secret_wrapper() {
		while(check_flag()) {
			for(Integer a : new_pr.keySet()) {
				pre_pr.put(a, new_pr.get(a));
			}
			secret();
		}
	}
	
	private boolean check_flag() {
		for(Integer a: new_pr.keySet()) {
			Double new_pr_value = new_pr.get(a);
			Double old_pr_value = pre_pr.get(a);
			Double diff = new_pr_value - old_pr_value;			
			if(Math.abs(diff) > 0.000001) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Integer> find_neighbours(int a) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i=1; i < adjMartrix.length; i++) {
			if(adjMartrix[a][i] == 1) {
				result.add(i);
			}
		}
		return result;
	}
	
	private int find_outDegree(int a) {
		int result = 0;
		for(int i=1; i < adjMartrix.length; i++) {
			if(adjMartrix[i][a] == 1) {
				result++;
			}
		}
		if(result == 0) {
			return 1;
		}
		return result;
	}
	
	public void printResult() {
		for(String a : vertex_num.keySet()) {
			int value = vertex_num.get(a);
			Double value_pr = new_pr.get(value);
			result.put(a, value_pr);
		}
		
		for(String a : result.keySet()) {
			System.out.println(a + "\t" + result.get(a));
		}
	}
	
	public static void main(String args[]) {
		pageRank pr = new pageRank(args[0]);
		pr.readData();
		pr.initial();
		pr.secret();
		pr.secret_wrapper();
//		pr.test();
		pr.printResult();
	}

	
	public void test() {
//		System.out.println("the number of vertex is: " + numVertices);
//		System.out.println("the number of edges is: " + numEdges);
		for(String s : vertex_num.keySet()) {
			System.out.println(s + " " + vertex_num.get(s));
		}
		System.out.println();
		for(Integer s : num_vertex.keySet()) {
			System.out.println(s + " " + num_vertex.get(s));
		}
		System.out.println();
		for(Integer s : pre_pr.keySet()) {
			System.out.println(s + " " + pre_pr.get(s));
		}
		System.out.println();
		for(Integer s : new_pr.keySet()) {
			System.out.println(s + " " + new_pr.get(s));
		}
		System.out.println();
	}	
}
