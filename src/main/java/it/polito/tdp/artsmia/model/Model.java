package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	List<Adiacenza> archi;
	
	//ricorsione
	private List<Integer> best;
	
	public Model() {
		dao=new ArtsmiaDAO();
		
	}

	public List<String> getRuoli() {
		
		return dao.getRuoli();
	}
	
	public void creaGrafo(String ruolo) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		
		Graphs.addAllVertices(this.grafo, dao.getArtisti(ruolo));
		
		
		archi=this.dao.getArchi(ruolo);
		for (Adiacenza a : archi) {
			if (this.grafo.vertexSet().contains(a.getId1()) && 
					this.grafo.vertexSet().contains(a.getId2())) {
				Graphs.addEdge(this.grafo, a.getId1(), a.getId2(), a.getPeso());
			}
		}
		
		System.out.println("#Vertici: "+this.grafo.vertexSet().size());
		System.out.println("#Archi: "+this.grafo.edgeSet().size());
	}
	public int vertici() {
		return this.grafo.vertexSet().size();
	}
	public int archi() {
		return this.grafo.edgeSet().size();
	}
	public List<Adiacenza> getAdiacenze(){
		return this.archi;
	}
	
	//Ricorsione
	public List<Integer> trovaPercorso(Integer sorgente){
		best=new ArrayList<>();
		List<Integer> parziale=new ArrayList<>();
		parziale.add(sorgente);
		//lancio ricorsione
		ricorsione(parziale, -1); //do -1 che cosi so che quel peso non
		//ce e sono al livello 1
		return best;
	}
//peso con cui continua ricorsione
	private void ricorsione(List<Integer> parziale, int peso) {
		
		Integer lastInserito=parziale.get(parziale.size()-1);
		//ottengo i vicini
		List<Integer> vicini=Graphs.neighborListOf(this.grafo, lastInserito);
		for(Integer vicino:vicini) {
			
			if(!parziale.contains(vicino) && peso==-1) {
				parziale.add(vicino);
				ricorsione(parziale, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(lastInserito, vicino)));
				parziale.remove(vicino);
			} else {
				//a questo punto non aggiungo un vicino qualsiasi come
				//per sorgente, ma devo predere quello con il peso scelto
				if(!parziale.contains(vicino) && this.grafo.getEdgeWeight(this.grafo.getEdge(lastInserito, vicino))==peso) {
					parziale.add(vicino);
					ricorsione(parziale, peso);//ormai so che è peso
					parziale.remove(vicino);
				}
			}
			if(parziale.size()>best.size()) {
				this.best=new ArrayList<>(parziale);
			}
			
		}
		
	}
	public boolean grafoContiene(Integer i) {
		if(this.grafo.containsVertex(i)) {
			return true;
		}
		return false;
	}
	
}
