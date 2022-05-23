package it.polito.tdp.artsmia.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;
import it.polito.tdp.artsmia.db.DBConnect;

public class Model {

	private Graph<ArtObject,DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	//IDENTITY MAP
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		// va bene fare la new qua se devo interagire con il grafo solo una volta -> nel caso in cui avessi bisogno di più 
		//interazioni, devo pulire il grafo, quindi creo un metodo per cancellare tutti i dati.
		//oppure lo creo direttamente in un metodo apposito
		//grafo = new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer, ArtObject>();
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici -> tutti gli oggetti del database
		
		//1: recupero tutto gli oggetti dal DB (c'è già il metodo preformato)
		dao.listObjects(idMap);
		//2: li inserisco come vertici
		Graphs.addAllVertices(grafo, idMap.values());
		
		//AGGIUNGO ARCHI
		
		/*METODO 1: doppio ciclo for annidato -> confronto le coppie di vertici, verifico se si devono collegare e con quale peso
		 e in caso creo l'arco
		
		for(ArtObject a1 : this.grafo.vertexSet()) {
			for(ArtObject a2 : this.grafo.vertexSet()) {
				if(!a1.equals(a2) && !this.grafo.containsEdge(a1,a2)) { //ovviamente non devo controllare lo stesso vertice 
					//controllo anche che non ci sia già un arco tra  idue perché è un grafo non orientato 
					
					//se sono qui, controllo se devo collegrare a1 e a2 -> metodo nel dao che mi dice se sono collegati
					int peso = dao.getPeso(a1,a2);
					if(peso > 0 ) {
						//aggiungo arco
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		}
		System.out.println("GRAFO CREATO");
		System.out.println("# NUMERO VERITICI: "+grafo.vertexSet().size());
		System.out.println("# NUMERO ARCHI: "+grafo.edgeSet().size());*/
		
		
		//CON IL METODO 1 NON STAMPERA' MAI NULLA !!!!
		
		
		//METODO 2 -> BLOCCO UNO DEI DUE OGGETTI E CERCO DI FARMI DARE DAL DATABASE TUTTI GLI OGGETTI COLLEGATI AD ESSO
		
		/*QUESTA é LA QUEERY:
		 * 
		 *  SELECT e2.`object_id`, count(*) as peso
			FROM exhibition_objects e1, exhibition_objects e2
			WHERE e1.`exhibition_id` = e2.`exhibition_id` AND e1.`object_id` = 8485 AND e1.`object_id`!=e2.`object_id`
			GROUP BY e2.`object_id`
		 * 
		 *  So già che ci metterà un'ora quindi passo direttamente al metodo 3  */
		
		
		//METODO 3 -> PRENDO DIRETTAMENTE TUTTE LE COPPIE DI OGGETTI 
		for(Adiacenza a : dao.getAdiacenze()) {
			Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		}
		System.out.println("GRAFO CREATO");
		System.out.println("# NUMERO VERITICI: "+grafo.vertexSet().size());
		System.out.println("# NUMERO ARCHI: "+grafo.edgeSet().size());
}		
		
}
