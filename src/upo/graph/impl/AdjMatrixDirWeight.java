
package upo.graph.impl;


import java.util.NoSuchElementException;

import upo.graph.base.WeightedGraph;


public class AdjMatrixDirWeight extends AdjMatrixDir implements WeightedGraph{

	//AdjMatrixDirWeight eredita tutti i metodi da AdjMatrixDir i quali sono stati sviluppati in modo tale da essere adattabili sia per grafo pesato che non!
	public AdjMatrixDirWeight() {
		super(); 
	} 
	
	@Override
	public double getEdgeWeight(String sourceVertex, String targetVertex) throws IllegalArgumentException, NoSuchElementException {
		int sourceIndex = getVertexIndex(sourceVertex), targetIndex = getVertexIndex(targetVertex);
		if(matrix[sourceIndex][targetIndex] != Double.POSITIVE_INFINITY) return matrix[sourceIndex][targetIndex];
		else throw new IllegalArgumentException("Arco senza peso!");
	}

	@Override 
	public void setEdgeWeight(String sourceVertex, String targetVertex, double weight) throws IllegalArgumentException, NoSuchElementException {
		int sourceIndex = getVertexIndex(sourceVertex), targetIndex = getVertexIndex(targetVertex);
		if(containsEdge(sourceVertex,targetVertex)) matrix[sourceIndex][targetIndex] = weight;
		else throw new IllegalArgumentException("Arco non esistente!");
	}
	
	@Override
	public WeightedGraph getBellmanFordShortestPaths(String startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("getBellmanFordShortestPaths");
	}
	
	@Override
	public WeightedGraph getDijkstraShortestPaths(String startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("getDijkstraShortestPaths");
	}

	@Override
	public WeightedGraph getPrimMST(String startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("getPrimMST");
	}

	@Override
	public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("getKruskalMST");
	}
	
	@Override
	public WeightedGraph getFloydWarshallShortestPaths() {
	    int n = vertexList.size();
	    double[][] dist = new double[n][n];
	    int[][] next = new int[n][n];

	    for (int i = 0; i < n; i++) {
	        for (int j = 0; j < n; j++) {
	            if (i == j) {
	                dist[i][j] = 0;
	                next[i][j] = -1;
	            } else if (matrix[i][j] != Double.POSITIVE_INFINITY) {
	                dist[i][j] = matrix[i][j];
	                next[i][j] = j;
	            } else {
	                dist[i][j] = Double.POSITIVE_INFINITY;
	                next[i][j] = -1;
	            }
	        }
	    }

	    for (int k = 0; k < n; k++) {
	        for (int i = 0; i < n; i++) {
	            for (int j = 0; j < n; j++) {
	                if (dist[i][k] != Double.POSITIVE_INFINITY && dist[k][j] != Double.POSITIVE_INFINITY) {
	                    double newDist = dist[i][k] + dist[k][j];
	                    if (newDist < dist[i][j]) {
	                        dist[i][j] = newDist;
	                        next[i][j] = next[i][k];
	                    }
	                }
	            }
	        }
	    }

	    AdjMatrixDirWeight shortestPathsGraph = new AdjMatrixDirWeight();
	    for (int i = 0; i < n; i++) {
	        shortestPathsGraph.addVertex(vertexList.get(i));
	    }
	    for (int i = 0; i < n; i++) {
	        for (int j = 0; j < n; j++) {
	            if (i != j && next[i][j] != -1) {
	                String source = vertexList.get(i);
	                String destination = vertexList.get(j);
	                double weight = dist[i][j];
	                shortestPathsGraph.addEdge(source, destination);
	                shortestPathsGraph.setEdgeWeight(source, destination, weight);
	            }
	        }
	    }

	    return shortestPathsGraph;
	}
}
