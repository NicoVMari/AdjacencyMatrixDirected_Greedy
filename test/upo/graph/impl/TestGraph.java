package upo.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import upo.graph.base.Graph;
import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;

class TestGraph {
	private Graph grafo;
	
	@BeforeEach
	public void testInitGraph() {
		grafo = new AdjMatrixDir(); 
	}
	
	@Test
	public void testAddVertex() {
		assertEquals(1, grafo.addVertex("A"));
		assertEquals(2, grafo.addVertex("B"));
		assertEquals(3, grafo.addVertex("C"));
		assertEquals(4, grafo.addVertex("D"));
		assertEquals(5, grafo.addVertex("E"));
		assertEquals(-1, grafo.addVertex("A"));
	}
	

	@Test
	public void testContainsVertex() {
		
		grafo.addVertex("A");
		grafo.addVertex("B");
		
		assertTrue(grafo.containsVertex("A"));
		assertTrue(grafo.containsVertex("B"));
		assertFalse(grafo.containsVertex("C"));
	}
	
	@Test
	public void testGetVertexIndex() {	
		grafo.addVertex("A");
		grafo.addVertex("B");
		
		assertEquals(0, grafo.getVertexIndex("A"));
		assertEquals(1, grafo.getVertexIndex("B"));
		
		NoSuchElementException ex1 = assertThrows(NoSuchElementException.class, () -> grafo.getVertexIndex("C"));
		assertEquals("Label C non trovata!",ex1.getMessage());
	}
	
	@Test
	public void testGetVertexLabel() {
		grafo.addVertex("A"); 
		grafo.addVertex("B");
		
		assertEquals("A", grafo.getVertexLabel(0));
		assertEquals("B", grafo.getVertexLabel(1));
		
		IndexOutOfBoundsException ex1 = assertThrows(IndexOutOfBoundsException.class, () -> grafo.getVertexLabel(2));
		assertEquals("Indice 2 non trovato!",ex1.getMessage());
		
		IndexOutOfBoundsException ex2 = assertThrows(IndexOutOfBoundsException.class, () -> grafo.getVertexLabel(-1));
		assertEquals("Indice -1 non trovato!",ex2.getMessage());
	}
	
	@Test
	public void testRemoveVertex() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "C");
		grafo.removeVertex("B");
		
		NoSuchElementException ex1 = assertThrows(NoSuchElementException.class, () -> grafo.containsEdge("A", "B"));
		NoSuchElementException ex2 = assertThrows(NoSuchElementException.class, () -> grafo.containsEdge("B", "C"));
		
		assertFalse(grafo.containsVertex("B"));
		assertEquals("Label B non trovata!",ex1.getMessage());
		assertEquals("Label B non trovata!",ex2.getMessage());
		assertEquals(2, grafo.size());
	}
	
	@Test
	public void testAddEdge() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "C");
		
		IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> grafo.addEdge("A", "B"));
		IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> grafo.addEdge("B", "C"));	
		
		assertEquals("Arco già esistente!",ex1.getMessage());
		assertEquals("Arco già esistente!",ex2.getMessage());
	}
	
	@Test
	public void testContainsEdge() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addEdge("A", "B");
		
		assertTrue(grafo.containsEdge("A", "B"));
		assertFalse(grafo.containsEdge("B", "A"));
		assertFalse(grafo.containsEdge("A", "A"));
	}
	
	@Test
	public void testRemoveEdge() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addEdge("A", "B");
		assertTrue(grafo.containsEdge("A", "B"));
		grafo.removeEdge("A", "B");
		assertFalse(grafo.containsEdge("A", "B"));
		
		IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> grafo.removeEdge("A", "B"));
		assertEquals("Arco già rimosso!",ex1.getMessage());
	} 
	
	@Test
	public void testGetAdjacent() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		grafo.addVertex("E");
		
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "A");
		grafo.addEdge("A", "D");
		grafo.addEdge("D", "A");
		grafo.addEdge("B", "D");
		grafo.addEdge("D", "B");
		grafo.addEdge("D", "E");
		grafo.addEdge("E", "D");
		grafo.addEdge("E", "C");
		grafo.addEdge("C", "E");
		
		assertFalse(grafo.isAdjacent("C","A"));
		assertFalse(grafo.isAdjacent("A","A"));
		
		Set<String> setAdj = grafo.getAdjacent("A");
		for(String findAdj : setAdj) {
			assertTrue(grafo.isAdjacent(findAdj,"A"));
		}
	} 
	
	@Test
	public void testIsDirected() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		grafo.addVertex("E");
		
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "A");
		grafo.addEdge("A", "D");
		grafo.addEdge("D", "A");
		grafo.addEdge("B", "D");
		grafo.addEdge("D", "B");
		grafo.addEdge("D", "E");
		grafo.addEdge("E", "D");
		grafo.addEdge("E", "C");
		
		assertTrue(grafo.isDirected());
		grafo.addEdge("C", "E");
		assertFalse(grafo.isDirected());
	} 
	
	@Test
	public void testIsCyclic() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "C");
		
		assertFalse(grafo.isCyclic());
		grafo.addEdge("C", "A");
		assertTrue(grafo.isCyclic());
	} 
	
	@Test
	public void testIsDAG() {
		assertFalse(grafo.isDAG());
	
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "A");
		
		assertFalse(grafo.isDAG());
		grafo.removeEdge("B", "A");
		
		grafo.addEdge("B", "C");
		
		assertTrue(grafo.isDAG());
		grafo.addEdge("C", "B");
		assertFalse(grafo.isDAG());
		grafo.removeEdge("C", "B");
		grafo.addEdge("C", "A");
		assertFalse(grafo.isDAG());
	}
	
	@Test
	public void testGetBFSTree() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "A");
		grafo.addEdge("A", "D");
		grafo.addEdge("D", "A");
		grafo.addEdge("B", "D");
		grafo.addEdge("D", "B");
		grafo.addEdge("D", "C");
		grafo.addEdge("C", "D");
		
		
        VisitForest bfs = grafo.getBFSTree("A");
        
        assertEquals(Color.BLACK, bfs.getColor("A"));
        assertEquals(Color.BLACK, bfs.getColor("B"));
        assertEquals(Color.BLACK, bfs.getColor("C"));
        assertEquals(Color.BLACK, bfs.getColor("D"));
        
        assertEquals("A", bfs.getPartent("B"));
        assertEquals("A", bfs.getPartent("D"));
        assertEquals("D", bfs.getPartent("C"));
        
        assertEquals(1, bfs.getStartTime("A"));
        assertEquals(2, bfs.getStartTime("B"));
        assertEquals(3, bfs.getStartTime("D"));
        assertEquals(4, bfs.getEndTime("A"));
        assertEquals(5, bfs.getEndTime("B"));
        assertEquals(6, bfs.getStartTime("C"));
        assertEquals(7, bfs.getEndTime("D"));
        assertEquals(8, bfs.getEndTime("C"));  
	}
	
	@Test
	public void testGetDFSTree() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "A");
		grafo.addEdge("A", "D");
		grafo.addEdge("D", "A");
		grafo.addEdge("B", "D");
		grafo.addEdge("D", "B");
		grafo.addEdge("D", "C");
		grafo.addEdge("C", "D");
		
		
        VisitForest dfs = grafo.getDFSTree("A");
        
        assertEquals(Color.BLACK, dfs.getColor("A"));
        assertEquals(Color.BLACK, dfs.getColor("B"));
        assertEquals(Color.BLACK, dfs.getColor("C"));
        assertEquals(Color.BLACK, dfs.getColor("D"));
        
        assertEquals("A", dfs.getPartent("B"));
        assertEquals("B", dfs.getPartent("D"));
        assertEquals("D", dfs.getPartent("C"));
        
        assertEquals(1, dfs.getStartTime("A"));
        assertEquals(2, dfs.getStartTime("B"));
        assertEquals(3, dfs.getStartTime("D"));
        assertEquals(4, dfs.getStartTime("C"));
        assertEquals(5, dfs.getEndTime("C"));
        assertEquals(6, dfs.getEndTime("D"));
        assertEquals(7, dfs.getEndTime("B"));
        assertEquals(8, dfs.getEndTime("A"));
	}
	
	@Test
	public void testGetDFSForest() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		grafo.addVertex("E");
		grafo.addVertex("F");
		
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "A");
		grafo.addEdge("A", "D");
		grafo.addEdge("D", "A");
		grafo.addEdge("B", "D");
		grafo.addEdge("D", "B");
		grafo.addEdge("D", "C");
		grafo.addEdge("C", "D");
		
		grafo.addEdge("E", "F");
		grafo.addEdge("F", "E");
		
		
        VisitForest dfs = grafo.getDFSTOTForest("A");
        
        assertEquals(Color.BLACK, dfs.getColor("A"));
        assertEquals(Color.BLACK, dfs.getColor("B"));
        assertEquals(Color.BLACK, dfs.getColor("C"));
        assertEquals(Color.BLACK, dfs.getColor("D"));
        
        assertEquals("A", dfs.getPartent("B"));
        assertEquals("B", dfs.getPartent("D"));
        assertEquals("D", dfs.getPartent("C"));
        
        assertEquals("E", dfs.getPartent("F"));
	}
	
	@Test
	public void testGetDFSForestOrdering() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		grafo.addVertex("E");
		grafo.addVertex("F");
		
		grafo.addEdge("A", "B");
		grafo.addEdge("B", "A");
		grafo.addEdge("A", "D");
		grafo.addEdge("D", "A");
		grafo.addEdge("B", "D");
		grafo.addEdge("D", "B");
		grafo.addEdge("D", "C");
		grafo.addEdge("C", "D");
		
		grafo.addEdge("E", "F");
		grafo.addEdge("F", "E");
		
		
		String[] vertexOrdering = new String[] {"A", "D","E"};
        VisitForest dfs = grafo.getDFSTOTForest(vertexOrdering);
        
        assertEquals(Color.BLACK, dfs.getColor("A"));
        assertEquals(Color.BLACK, dfs.getColor("B"));
        assertEquals(Color.BLACK, dfs.getColor("C"));
        assertEquals(Color.BLACK, dfs.getColor("D"));
        
        assertEquals("A", dfs.getPartent("B"));
        assertEquals("B", dfs.getPartent("D"));
        assertEquals("D", dfs.getPartent("C"));
        
        assertEquals("E", dfs.getPartent("F"));
	}
	
	@Test
	public void testTopologicalSort() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		grafo.addVertex("E");
		grafo.addVertex("F");
		
		grafo.addEdge("A", "B");
		
		grafo.addEdge("C", "D");
		grafo.addEdge("C", "E");
		grafo.addEdge("E", "C");
		grafo.addEdge("F", "E");
		
		UnsupportedOperationException ex1 = assertThrows(UnsupportedOperationException.class, () -> grafo.topologicalSort());
		assertEquals("Grafo non DAG!",ex1.getMessage());
		grafo.removeEdge("E", "C");
		
		String[] topologicalSort = grafo.topologicalSort();
		assertEquals("F",topologicalSort[0]);
		assertEquals("C",topologicalSort[1]);
		assertEquals("E",topologicalSort[2]);
		assertEquals("D",topologicalSort[3]);
		assertEquals("A",topologicalSort[4]);
		assertEquals("B",topologicalSort[5]);
	}
	
	@Test
	public void testConnectedComponents() {
		grafo.addVertex("0");grafo.addVertex("1");grafo.addVertex("2");grafo.addVertex("3");grafo.addVertex("4");grafo.addVertex("5");grafo.addVertex("6");grafo.addVertex("7");grafo.addVertex("8");grafo.addVertex("9");
		
		grafo.addEdge("0", "1");grafo.addEdge("0", "5");grafo.addEdge("0", "4");grafo.addEdge("1", "0");grafo.addEdge("1", "5");grafo.addEdge("5", "0");grafo.addEdge("5", "1");grafo.addEdge("4", "0");
		
		grafo.addEdge("8", "9");grafo.addEdge("9", "8");
		
		grafo.addEdge("2", "3");grafo.addEdge("2", "6");grafo.addEdge("6", "2");grafo.addEdge("3", "2");
		
		Set<Set<String>> connectedComponents = grafo.connectedComponents();
		
		//{0, 1, 4, 5}, {2, 3, 6}, {7}, {8, 9}
		
		Set<Set<String>> expectedComponents = new HashSet<>();
	    expectedComponents.add(new HashSet<>(Arrays.asList("0", "1", "4", "5")));
	    expectedComponents.add(new HashSet<>(Arrays.asList("2", "3", "6")));
	    expectedComponents.add(new HashSet<>(Collections.singletonList("7")));
	    expectedComponents.add(new HashSet<>(Arrays.asList("8", "9")));

	    assertEquals(expectedComponents, connectedComponents);
	}
	
	@Test
	public void testStronglyConnectedComponents() {
		grafo.addVertex("A");
		grafo.addVertex("B");
		grafo.addVertex("C");
		grafo.addVertex("D");
		grafo.addVertex("E");
		grafo.addVertex("F");
		
		grafo.addEdge("A", "D");
		grafo.addEdge("D", "B");
		grafo.addEdge("D", "E");
		grafo.addEdge("B", "C");
		grafo.addEdge("C", "D");
		grafo.addEdge("E", "F");
		grafo.addEdge("F", "E");
		
		Set<Set<String>> stronglyConnectedComponents = grafo.stronglyConnectedComponents();
		
		
		//{A}, {D, B, C}, {E, F}
		
		Set<Set<String>> expectedComponents = new HashSet<>();
		expectedComponents.add(new HashSet<>(Collections.singletonList("A")));
		expectedComponents.add(new HashSet<>(Arrays.asList("B", "C", "D")));
		expectedComponents.add(new HashSet<>(Arrays.asList("E", "F")));

	    assertEquals(expectedComponents, stronglyConnectedComponents);
	}

}
