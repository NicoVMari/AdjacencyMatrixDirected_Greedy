package upo.graph.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import upo.graph.base.Graph;
import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;
import upo.graph.base.WeightedGraph;

//Tutti i metodi sono stati realizzati seguendo gli pseudo-codici presenti sulle slide
public class AdjMatrixDir implements Graph{
	protected double[][] matrix; 
	protected ArrayList<String> vertexList;
	
	public AdjMatrixDir() {
		this.matrix = new double[0][0];
		this.vertexList = new ArrayList<>();   
	} 

	@Override 
	public int getVertexIndex(String label) { 
		if(vertexList.contains(label)) return vertexList.indexOf(label);
		else throw new NoSuchElementException("Label "+ label +" non trovata!");
	}

	@Override
	public String getVertexLabel(Integer index) {
		if(index < 0 || index >= this.size()) throw new IndexOutOfBoundsException("Indice "+ index + " non trovato!");
		else return vertexList.get(index);
	}

	@Override
	public int addVertex(String label) {
		if (vertexList.contains(label)) return -1;
	    else {
	    	vertexList.add(label);
	    	double[][] newMatrix = initializationMatrix();

	        for (int i = 0; i < vertexList.size()-1; i++) {
	            for (int j = 0; j < vertexList.size()-1; j++) {
	                newMatrix[i][j] = matrix[i][j];
	            }
	        } 
	        matrix = newMatrix;
	        return vertexList.size();
	    }
	}
	
	private double[][] initializationMatrix() {
		double[][] newMatrix = new double[vertexList.size()][vertexList.size()];
		
		for (int i = 0; i < vertexList.size(); i++) {
			for (int j = 0; j < vertexList.size(); j++) {
				if(i != j) newMatrix[i][j] = Double.POSITIVE_INFINITY;
			}
		}
		
		return newMatrix;
	}

	@Override
	public boolean containsVertex(String label) {
		return vertexList.contains(label);
	}

	@Override
	public void removeVertex(String label) throws NoSuchElementException {
		int index = getVertexIndex(label);
		vertexList.remove(index);
	    double[][] newMatrix = new double[vertexList.size()][vertexList.size()];
	    int row = 0, col = 0;
	    for (int i = 0; i < vertexList.size(); i++) {
	        if (i != index) {
	            for (int j = 0; j < vertexList.size(); j++) {
	                if (j != index) {
	                    newMatrix[row][col] = matrix[i][j];
	                    col++;
	                }
	            }
	            row++;
	            col = 0;
	        }
	    } 
	    matrix = newMatrix; 
	}
 
	@Override
	public void addEdge(String sourceVertex, String targetVertex) throws IllegalArgumentException {
		int sourceIndex = getVertexIndex(sourceVertex), targetIndex = getVertexIndex(targetVertex);
		if(matrix[sourceIndex][targetIndex] == Double.POSITIVE_INFINITY) matrix[sourceIndex][targetIndex] = WeightedGraph.defaultEdgeWeight;
		else throw new IllegalArgumentException("Arco già esistente!");
	}

	@Override
	public boolean containsEdge(String sourceVertex, String targetVertex) throws IllegalArgumentException {	
		int sourceIndex = getVertexIndex(sourceVertex), targetIndex = getVertexIndex(targetVertex);
		if(sourceIndex == targetIndex) return false;
		else return matrix[sourceIndex][targetIndex] != Double.POSITIVE_INFINITY;
	}

	@Override
	public void removeEdge(String sourceVertex, String targetVertex) throws IllegalArgumentException, NoSuchElementException {
		int sourceIndex = getVertexIndex(sourceVertex), targetIndex = getVertexIndex(targetVertex);
		if(matrix[sourceIndex][targetIndex] != Double.POSITIVE_INFINITY) matrix[sourceIndex][targetIndex] = Double.POSITIVE_INFINITY;
		else throw new IllegalArgumentException("Arco già rimosso!");
	}

	@Override
	public Set<String> getAdjacent(String vertex) throws NoSuchElementException {
		int index = getVertexIndex(vertex);
	    Set<String> setAdj = new HashSet<>(); 
	    for (int i = 0; i < vertexList.size(); i++) {
	        if (index != i && matrix[index][i] != Double.POSITIVE_INFINITY) setAdj.add(getVertexLabel(i));
	    }
	    return setAdj;
	}

	@Override
	public boolean isAdjacent(String targetVertex, String sourceVertex) throws IllegalArgumentException {
		int sourceIndex = getVertexIndex(sourceVertex), targetIndex = getVertexIndex(targetVertex);
		if(sourceIndex == targetIndex) return false;
		else return matrix[sourceIndex][targetIndex] != Double.POSITIVE_INFINITY; 
	}

	@Override
	public int size() {
		return vertexList.size();
	}

	@Override
	public boolean isDirected() {
	    for(int i = 0; i < vertexList.size(); i++) {
	        for(int j = i+1; j < vertexList.size(); j++) {
	            if(matrix[i][j] != matrix[j][i]) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	@Override
	public boolean isCyclic() {
		VisitForest visit = new VisitForest(this, VisitType.BFS);
	    for (String vertex : vertexList) {
	        if (visit.getColor(vertex) == Color.WHITE) {
	            if (findCycle(vertex, visit)) {
	                return true;
	            }
	        }
	    }
	    return false;
		
	}
	
	private boolean findCycle(String vertex, VisitForest visit) {
		visit.setColor(vertex, Color.GRAY);
	    for (String adjVertex : getAdjacent(vertex)) {
	        if (visit.getColor(adjVertex) == Color.WHITE) {
	        	visit.setParent(adjVertex, vertex);
	            if (findCycle(adjVertex,visit)) {
	                return true;
	            }
	        } else if (visit.getColor(adjVertex) == Color.GRAY) {
	            return true;
	        }
	    }
	    visit.setColor(vertex, Color.BLACK); 
	    return false;
	}

	@Override
	public boolean isDAG() {
		return !isCyclic() && isDirected();
	}
	
	@Override
	public VisitForest getBFSTree(String startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
	    VisitForest visit = new VisitForest(this, VisitType.BFS);
	    Queue<String> queue = new LinkedList<>();
	    int time = 0;
	    visit.setColor(startingVertex, Color.GRAY);
	    visit.setStartTime(startingVertex, time = time + 1);
	    queue.add(startingVertex); 
	    while (!queue.isEmpty()) {
	        String currentVertex = queue.remove();
	        for (String adjVertex : getAdjacent(currentVertex)) { 
	            if (visit.getColor(adjVertex) == Color.WHITE) {
		                visit.setColor(adjVertex, Color.GRAY);
		                visit.setStartTime(adjVertex, time = time + 1);
		                visit.setParent(adjVertex, currentVertex);
		                queue.add(adjVertex);
	            }
	        }
	        visit.setColor(currentVertex, Color.BLACK);
	        visit.setEndTime(currentVertex, time = time + 1);
	    }
	    return visit;
	}
	
	@Override
	public VisitForest getDFSTree(String startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		VisitForest visit = new VisitForest(this, VisitType.DFS);
	    Stack<String> stack = new Stack<>();
	    int time = 0;
	    
	    visit.setColor(startingVertex, Color.GRAY);
	    visit.setStartTime(startingVertex, time = time+1);
	    stack.push(startingVertex); 
	    while (!stack.isEmpty()) {
	        String currentVertex = stack.peek();  
	        boolean nodeAdjWhite = false;
	        for (String adjVertex : getAdjacent(currentVertex)) {
	            if (visit.getColor(adjVertex) == Color.WHITE) {
		            	nodeAdjWhite = true;
		                visit.setColor(adjVertex, Color.GRAY);
		                visit.setStartTime(adjVertex, time = time+1);
		                visit.setParent(adjVertex, currentVertex);
		                stack.push(adjVertex);
		                break;
	            }
	        }
	        if(!nodeAdjWhite) {
	        	visit.setColor(currentVertex, Color.BLACK);
	        	visit.setEndTime(currentVertex, time = time + 1);
		        stack.pop(); 
	        }    
	    }
	    return visit;
	}

	@Override
	public VisitForest getDFSTOTForest(String startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
	    VisitForest visit = new VisitForest(this, VisitType.DFS_TOT);
	    visitDFSTOT(startingVertex, visit);
	    for (String vertex : vertexList) {
	        if (visit.getColor(vertex) == Color.WHITE) {
	        	visitDFSTOT(vertex, visit);
	        }
	    }
	    return visit;
	}

	private void visitDFSTOT(String currentVertex, VisitForest visit) {
	    visit.setColor(currentVertex, Color.GRAY);
	    for (String adjVertex : getAdjacent(currentVertex)) {
	        if (visit.getColor(adjVertex) == Color.WHITE) {
		            visit.setParent(adjVertex, currentVertex);
		            visitDFSTOT(adjVertex, visit);
	        }
	    }
	    visit.setColor(currentVertex, Color.BLACK);
	}

	@Override
	public VisitForest getDFSTOTForest(String[] vertexOrdering)throws UnsupportedOperationException, IllegalArgumentException {
		VisitForest visit = new VisitForest(this, VisitType.DFS_TOT);
	    for (String vertex : vertexOrdering) {
	        if (visit.getColor(vertex) == Color.WHITE) {
	        	visitDFSTOT(vertex, visit);
	        }
	    }
	    return visit;
	}
	
	@Override
	public String[] topologicalSort() throws UnsupportedOperationException {
		if (isDAG()) {
	        String[] sortLabel = new String[vertexList.size()];
	        int t = vertexList.size() - 1;
	        VisitForest visit = new VisitForest(this, VisitType.DFS);
	        for (String vertex : vertexList) {
	            if (visit.getColor(vertex) == Color.WHITE) {
	                t = getDFSTopological(vertex, visit, sortLabel, t); 
	            }
	        }
	        return sortLabel;
	    } else {
	        throw new UnsupportedOperationException("Grafo non DAG!");
	    }
	}
	
	private int getDFSTopological(String currentVertex, VisitForest visit, String[] sortLabel, int t) {
	    visit.setColor(currentVertex, Color.GRAY);
	    for (String adjVertex : getAdjacent(currentVertex)) {
	        if (visit.getColor(adjVertex) == Color.WHITE) {
		            visit.setParent(adjVertex, currentVertex);
		            t = getDFSTopological(adjVertex, visit, sortLabel, t);
	        }
	    }
	    visit.setColor(currentVertex, Color.BLACK);
	    sortLabel[t] = currentVertex;
	    return t - 1;
	}

	@Override
	public Set<Set<String>> stronglyConnectedComponents() throws UnsupportedOperationException {
		Set<Set<String>> stronglyConnectedComponents = new HashSet<>();
		
		VisitForest visit = new VisitForest(this, VisitType.DFS);
	    Queue<String> queue = new LinkedList<>();
	    Stack<String> stack = new Stack<>();
	    String startingVertex = vertexList.get(0);
	    
		visit.setColor(startingVertex , Color.GRAY);
		stack.push(startingVertex);
		queue.add(startingVertex);
		
		//1)
	    while (!stack.isEmpty()) {
	        String currentVertex = stack.peek();  
	        boolean nodeAdjWhite = false;
	        for (String adjVertex : getAdjacent(currentVertex)) {
	            if (visit.getColor(adjVertex) == Color.WHITE) {
		                visit.setColor(adjVertex, Color.GRAY);
		                visit.setParent(adjVertex, currentVertex);
		                stack.push(adjVertex);
		                queue.add(adjVertex);
		                nodeAdjWhite = true;
		                break;
	            }
	        }
	        if(!nodeAdjWhite) {
	        	visit.setColor(currentVertex, Color.BLACK);
	        	stack.pop(); 
	        }    
	    }
	    
	    //2) Matrice trasposta
	    AdjMatrixDir transposeMatrix = new AdjMatrixDir();
	    for (String vertex : vertexList) {
	    	transposeMatrix.addVertex(vertex);
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (i != j && matrix[i][j] != Double.POSITIVE_INFINITY) {
					transposeMatrix.addEdge(vertexList.get(j), vertexList.get(i));
				}
			}
		}
		
		//3)
		Set<String> visited = new HashSet<>();
		while (!queue.isEmpty()) {
			String vertex = queue.remove();
			if (!visited.contains(vertex)) { 
					Set<String> component = new HashSet<>();
					visited.add(vertex);
					component.add(vertex);
					for (String adjVertex : transposeMatrix.getAdjacent(vertex)) {
						if (!visited.contains(adjVertex)) {
							kosarajuTransposeDFS(adjVertex, visited, component, transposeMatrix);
						}
					}
					stronglyConnectedComponents.add(component);
			}
		}
	    
	    return stronglyConnectedComponents;
	}
	
	private void kosarajuTransposeDFS(String vertex, Set<String> visited, Set<String> component, AdjMatrixDir transpose) {
		visited.add(vertex);
		component.add(vertex); 
		for (String adjVertex : transpose.getAdjacent(vertex)) {
			if (!visited.contains(adjVertex)) {
				kosarajuTransposeDFS(adjVertex, visited, component, transpose);
			}
		}
	}
	
	@Override
	public Set<Set<String>> connectedComponents() throws UnsupportedOperationException {
		VisitForest visit = new VisitForest(this, VisitType.DFS);
	    Set<Set<String>> connectedForest = new HashSet<>();
	    for (String vertex : vertexList) {
	        if (visit.getColor(vertex) == Color.WHITE) {
	            Set<String> connectedTree = new HashSet<>();
	    	    Stack<String> stack = new Stack<>();
	    	    visit.setColor(vertex, Color.GRAY);
	    	    stack.push(vertex);
	    	    connectedTree.add(vertex);
	    	    while (!stack.isEmpty()) {
	    	        String currentVertex = stack.peek();    
	    	        for (String adjVertex : getAdjacent(currentVertex)) {
	    	            if (visit.getColor(adjVertex) == Color.WHITE) {
		    	                visit.setColor(adjVertex, Color.GRAY);
		    	                visit.setParent(adjVertex, currentVertex);
		    	                stack.push(adjVertex);
		    	                connectedTree.add(adjVertex);
	    	            }
	    	        }
	    	        visit.setColor(currentVertex, Color.BLACK);
	    	        stack.pop();
	    	    }
	            connectedForest.add(connectedTree);
	        }
	    }
	    return connectedForest;
	}
}