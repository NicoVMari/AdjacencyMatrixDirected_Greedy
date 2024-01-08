package upo.graph.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Greedy {
	
    private static class Node implements Comparable<Node> {
        private final Character characters;
        private final int frequency;
        private final Node left, right;

        Node(Character characters, int f, Node left, Node right) {
            this.characters    = characters;
            this.frequency  = f;
            this.left  = left;
            this.right = right;
        }

        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }
	
	/** Calcola una codifica di Huffman per i caratteri contenuti nel vettore characters, date le frequenze 
	 * contenute in f. f[i] contiene la frequenza (in percentuale, 0<=f[i]<=100) del carattere characters[i] 
	 * nel testo per il quale si vuole calcolare la codifica.
	 * </br>CONSIGLIO: potete estendere o usare un vostro grafo non pesato non orientato per rappresentare la 
	 * foresta di Huffman.
	 * </br>CONSIGLIO2: potete implementate una PriorityQueue dall'interfaccia in upo.additionalstructures,
	 * oppure aggiungere al grafo del primo consiglio delle priorit�.
	 * 
	 * @param characters i caratteri dell'alfabeto per i quali calcolare la codifica.
	 * @param f le frequenze dei caratteri in characters nel dato testo.
	 * @return una Map che mappa ciascun carattere in una stringa che rappresenta la sua codifica secondo 
	 * l'algoritmo visto a lezione.
	 */
	public static Map<Character, String> getHuffmanCodes(Character[] characters, int[] f) {
	    PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
	    for (int i = 0; i < characters.length; i++) {
	    	Node node = new Node(characters[i], f[i], null, null);
	        priorityQueue.add(node);
	    }

	    while (priorityQueue.size() > 1) {
	    	Node x = priorityQueue.poll();
	        Node y = priorityQueue.poll();
	        Node z = new Node(null, x.frequency + y.frequency, x, y);
	        priorityQueue.add(z);
	    }
	    
	    Map<Character, String> huffmanCodes = new HashMap<>();
	    generateCodes(priorityQueue.poll(), "", huffmanCodes);

	    return huffmanCodes;
	}

	private static void generateCodes(Node node, String code, Map<Character, String> huffmanCodes) {
	    if (node == null) return;
	    if (node.isLeaf()) {
	        huffmanCodes.put(node.characters, code);
	    }

	    generateCodes(node.left, code + "0", huffmanCodes);
	    generateCodes(node.right, code + "1", huffmanCodes);
	}
	
	/** Trova il massimo insieme di intervalli disgiunti, tra tutti quelli identificati da [starting[i], ending[i]],
	 * (0<=i<=starting.length) utilizzando l'algoritmo Greedy. Il risultato contiene gli indici degli intervalli selezionati.
	 * Ad esempio, con starting=[2,5,6] ed ending=[5,7,8] il risultato sar� uguale a [0,2] perch� il massimo insieme
	 * di intervalli disgiunti � {[2,5],[6,8]}.
	 * 
	 * @param starting il vettore dei tempi di inizio degli intervalli
	 * @param ending il vettore dei tempi di fine degli intervalli
	 * @return un vettore contenente gli indici del massimo insieme di intervalli disgiunti
	 */
	public static Integer[] getMaxDisjointIntervals(Integer[] starting, Integer[] ending) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/** Trova lo scheduling massimale, utilizzando l'algoritmo di Moore, tra i job identificati dai vettori duration e deadline
	 * (duration[i] e deadline[i] sono, rispettivamente, la durata e la scadenza del job L_i). Il risultato contiene, nell'ordine
	 * selezionato dall'algoritmo, gli indici dei job nello scheduling massimale.
	 * 
	 * @param duration il vettore delle durate
	 * @param deadline il vettore delle scadenze
	 * @return un vettore contenente gli indici dei job in uno scheduling massimale
	 */
	public static Integer[] getMooreMaxJobs(Integer[] duration, Integer[] deadline) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
