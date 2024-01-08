package upo.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class TestHuffman {

	 @Test
    public void testGetHuffmanCodes() {
        Character[] characters = {'a', 'b', 'c', 'd','e','f'};
        int[] frequencies = {45, 13, 12, 16, 9, 5};

        Map<Character, String> huffmanCodes = Greedy.getHuffmanCodes(characters, frequencies);

        assertEquals("0", huffmanCodes.get('a'));
        assertEquals("101", huffmanCodes.get('b'));
        assertEquals("100", huffmanCodes.get('c'));
        assertEquals("111", huffmanCodes.get('d'));
        assertEquals("1101", huffmanCodes.get('e'));
        assertEquals("1100", huffmanCodes.get('f'));
    }

}
