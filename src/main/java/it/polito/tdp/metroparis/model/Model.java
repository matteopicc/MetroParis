package it.polito.tdp.metroparis.model;
import java.util.HashMap;
//esiste ALMENO una connessione tra partenza ed arrivo
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata,DefaultEdge>grafo;
	
	//meglio creare un metodo creaGrafo per ricreare il grafico da zero ogni volta dato che potrebbe cambiare
	public void creaGrafo() {
		this.grafo = new SimpleDirectedGraph<Fermata,DefaultEdge>(DefaultEdge.class); 
		
		MetroDAO dao = new MetroDAO();		
		List<Fermata>fermate = dao.getAllFermate();
		Map<Integer,Fermata> fermateIdMap = new HashMap<Integer,Fermata>();
		for(Fermata f : fermate) {
			fermateIdMap.put(f.getIdFermata(), f);
		}
		
		//this.grafo.addVertex() aggiunge un vertice alla volta
		Graphs.addAllVertices(this.grafo, fermate);
	/*	
		// metodo1 : itero su ogni coppia di vertici ( lunghissimo e molto lento)
		for(Fermata partenza: fermate) {
			for(Fermata arrivo : fermate) {
				if(dao.isFermateConnesse(partenza, arrivo)) {
					this.grafo.addEdge(partenza,arrivo);
				}
			}
		}
		
		// metodo2 : dato ciascun vertice, individua i vertici ad esso adiacenti
		for(Fermata partenza : fermate) {
			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
			for(Integer id : idConnesse) {
				//Fermata arrivo = fermata che possiede questo id
				Fermata arrivo = null;
				for(Fermata f: fermate) {
					if(f.getIdFermata() == id) {
						arrivo = f;
						break;
					}
				
					this.grafo.addEdge(partenza, arrivo);
			}
		}
		
		
		
	}*/
	
		/*
		//metodo2 variante 2b: il DAO restituisce una lista di oggetti FERMATA
		for(Fermata partenza : fermate) {
			List<Fermata>arrivi = dao.getFermateConnesse(partenza);
			for(Fermata arrivo : arrivi) {
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/
		
		/*
		//metodo 2c: il DAO restituisce un elenco di ID numerici, che converto in oggetti tramite una hash map
		for(Fermata partenza : fermate) {
			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
			for(int id : idConnesse) {
				Fermata arrivo = fermateIdMap.get(id);
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/
		
		
		
		//metodo3: faccio una sola query che mi restituisca tutte le coppie di fermate da collegare. Variante preferita 3c: con identity map
		List<CoppiaId>fermateDaCollegare = dao.getAllFermateConnesse();
		for(CoppiaId coppia : fermateDaCollegare) {
			this.grafo.addEdge(fermateIdMap.get(coppia.getIdPartenza()), fermateIdMap.get(coppia.getIdArrivo()));
			
		}
		System.out.println(this.grafo);
		System.out.println("Vertici = "+this.grafo.vertexSet().size());
		System.out.println("Archi = "+this.grafo.edgeSet().size());
		
	}
	
	
	}


