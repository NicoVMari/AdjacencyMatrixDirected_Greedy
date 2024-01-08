package upo.graph.impl;

import org.junit.jupiter.api.*;

import upo.graph.base.Graph;
import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestStructure{

    private static final String path = "upo.graph.impl.";
    private String matricola;
    private Graph grafo;

    @SuppressWarnings("deprecation")
	@BeforeAll
	@Test
    void setUp() throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?> datiClass = null; 
        Class<?> graphClass = null;
        String classe = null;
        String datiStudentePath = path+"DatiStudente";
        try {
            datiClass = Class.forName(path+"DatiStudente");
        } catch (ClassNotFoundException e) {
            Assertions.fail("Sembra che non esista la classe "+datiStudentePath+". Creala come richiesto nel testo del compito.");
        }
        try {
            this.matricola = (String) datiClass.getField("matricola").get(null);
        } catch (Exception e) {
            Assertions.fail("Sembra che la classe DatiStudente non abbia il campo matricola, o che questo non sia public static String. Per favore, correggi.");
        }
        switch (matricola.charAt(matricola.length()-1)) {
            case '0':
            case '1':
                classe = "AdjListUndir";
                break;
            case '2':
            case '3':
            case '8':
                classe = "AdjListDir";
                break;
            case '4':
            case '5':
                classe = "AdjMatrixUndir";
                break;
            case '6':
            case '7':
            case '9':
                classe = "AdjMatrixDir";
                break;
        }
        String fullClassNames[] = {path+classe, path+classe+"Weight"};
        for(String fullClassName : fullClassNames) {
            try {
                graphClass = Class.forName(fullClassName);
            } catch (ClassNotFoundException e) {
                Assertions.fail("Sembra che non esista la classe da implementare relativa alla tua matricola (dovrebbe essere " + fullClassName + ").");
            }
            try {
            	grafo = (Graph) graphClass.newInstance();
            } catch (ClassCastException e) {
                Assertions.fail("Sembra che la tua classe ("+fullClassName+") non estenda la classe Graph o WeightedGraph (a seconda di quale classe stiamo considerando).");
            } catch (InstantiationException e) {
                Assertions.fail("Sembra che la tua classe ("+fullClassName+") non abbia un costruttore senza parametri. Per favore implementalo.");
            } catch (IllegalAccessException e) {
                Assertions.fail("Ci sono problemi con l'accesso al jar fornito dal docente. Contatta il docente");

            }
        }
    } 
}
