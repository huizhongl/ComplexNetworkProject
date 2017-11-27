package project;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class betweennessCentrality {
	private int numVertices;
	private int numEdges;
	private HashSet<String> total_vertices;
	private int[][] adjMartrix;
	private HashMap<String,Double> finalResult;
	private String file_path;
	private HashMap<String, Integer> vertex_num;
	private HashMap<Integer, String> num_vertex;
	
	public betweennessCentrality(String path) {
		numVertices = 0;
		numEdges = 0;
		total_vertices = new HashSet<String>();
		adjMartrix = new int[1000][1000];
		finalResult = new HashMap<String,Double>();
		file_path = path;
		vertex_num = new HashMap<String, Integer>();
		num_vertex = new HashMap<Integer, String>();
	}
	
	public betweennessCentrality() {
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
	
	public void testData() {
		System.out.println("total vertices is: " + numVertices);
		System.out.println("total edges is: " + numEdges);
		
		for(String a : vertex_num.keySet()) {
			System.out.println(a + "\t" + vertex_num.get(a));
		}
	}
	
	public void betweenness() {
		for(int i = 0; i < numVertices; i++) {
			core(i);
		}
	}
	
private void core(int s) {
		
		int dist[] = new int[numVertices];
		int weight[] = new int[numVertices];
		double value[] = new double[numVertices];
		for(int i = 0; i < numVertices; i++) {
			dist[i] = Integer.MAX_VALUE;
			weight[i] = 0;
			value[i] = 0.0;
		}
		dist[s] = 0;
		weight[s] = 1;

		HashSet<Integer> visited = new HashSet<Integer>();
		Queue<Integer> myQue = new LinkedList<Integer>();
		visited.add(s); 
		myQue.add(s);
		while(!myQue.isEmpty()) {
			int cur = myQue.remove();
			for(int i=0; i < numVertices; i++) {
				if(adjMartrix[i][cur] == 1 || adjMartrix[cur][i] == 1) {
					if(!visited.contains(i)) {
						visited.add(i);
						myQue.add(i);
						dist[i] = dist[cur] + 1;
						weight[i] = weight[cur];
					} else if(visited.contains(i)) {
						if(dist[i] == dist[cur] + 1) {
							weight[i] = weight[i] + weight[cur];
						}
					}					
				}
			}
		}
		
		int maxDistance = 0;
		for(int i = 0; i < dist.length; i++) {
			if(dist[i] != Integer.MAX_VALUE && dist[i] > maxDistance) {
				maxDistance = dist[i];
			}
		}
		for(int i = 0; i < dist.length; i++) {
			if(dist[i] == maxDistance) {
				value[i] = 1.0;
			}
		}
		
		int layer = maxDistance;
		while(layer != 0) {
			for(int i = 0; i < dist.length; i++) {
				if(dist[i] == layer -1) {
					for(int j=0; j < numVertices; j++) {
						if((adjMartrix[i][j] == 1 || adjMartrix[j][i] == 1) && dist[j] == dist[i] + 1) {
							value[i] = value[i] + value[j] * weight[i] / weight[j];
						}
					}
					value[i] = value[i] + 1;
				}
			}
			layer = layer-1;
		}
		
		for(int i=0; i < value.length; i++) {
			if(!finalResult.containsKey(num_vertex.get(i))) {
				finalResult.put(num_vertex.get(i), value[i]);
			} else {
				double origin = finalResult.get(num_vertex.get(i));
				finalResult.put(num_vertex.get(i), origin + value[i]);
			}
		}				
	}
	
	public void printResult() {
		for(String a: finalResult.keySet()) {
			System.out.println("[" + a + "]" + "\t" + finalResult.get(a));
		}
	}


	public static void main(String args[]) {
		betweennessCentrality bc = new betweennessCentrality(args[0]);
		bc.readData();
//		bc.testData();
		bc.betweenness();
		bc.printResult();
	} 
	
}

