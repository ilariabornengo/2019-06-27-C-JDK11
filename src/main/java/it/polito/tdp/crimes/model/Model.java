package it.polito.tdp.crimes.model;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String,DefaultWeightedEdge> grafo;
	public List<String> percorsoBest;
	public int pesoTop;
	
	public Model()
	{
		this.dao=new EventsDao();
	}
	
	public void creaGrafo(String categoria,Date data)
	{
		this.grafo=new SimpleWeightedGraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(data, categoria));
		
		//aggiungo gli archi
		for(Adiacenza a:this.dao.getAdiacenze(data, categoria, this.dao.getVertici(data, categoria)))
		{
			if(this.grafo.vertexSet().contains(a.getId1())&& this.grafo.vertexSet().contains(a.getId2()))
			{
				Graphs.addEdge(this.grafo, a.getId1(), a.getId2(), a.getPeso());
			}
		}
	}
	
	public double getPesoMediano()
	{
		Integer min=Integer.MAX_VALUE;
		Integer max=Integer.MIN_VALUE;
		double pesoMedio=0.0;
		for(DefaultWeightedEdge d:this.grafo.edgeSet())
		{
			Integer peso=(int) this.grafo.getEdgeWeight(d);
			if(peso<min)
			{
				min=peso;
			}
			if(peso>max)
			{
				max=peso;
			}
		}
		pesoMedio=(max+min)/2;
		return pesoMedio;
	}
	
	public List<Adiacenza> getOrdinati(Date data,String categoria,double medio)
	{
		List<String> vertici=new ArrayList<>(this.grafo.vertexSet());
		List<Adiacenza> OK=new ArrayList<Adiacenza>();
		for(Adiacenza a:this.dao.getAdiacenze(data, categoria, vertici))
			{
				if(a.getPeso()<medio)
				{
					OK.add(a);
				}
			}
		return OK;
	}
	
	public List<String> percorsoMassimo(Adiacenza a)
	{
		this.percorsoBest=new ArrayList<>();
		List<String> parziale=new ArrayList<String>();
		String partenza=a.getId1();
		parziale.add(partenza);
		this.pesoTop=0;
		ricorsione(parziale,0,a);
		return this.percorsoBest;
	}
	
	private void ricorsione(List<String> parziale, int i,Adiacenza a) {
		String arrivo=a.getId2();
		String ultimo=parziale.get(parziale.size()-1);
		//caso terminale
		if(ultimo.equals(arrivo))
		{
			Integer pesoParz=getpeso(parziale);
			if(pesoParz>this.pesoTop)
			{
				this.pesoTop=pesoParz;
				this.percorsoBest=new ArrayList<>(parziale);
				return;
			}
		}
		
		//non caso terminale
		for(String vicino:Graphs.neighborListOf(this.grafo, ultimo))
		{
			if(!parziale.contains(vicino))
			{
				parziale.add(vicino);
				ricorsione(parziale,i+1,a);
				parziale.remove(parziale.size()-1);
				
			}
		}
		
	}

	private Integer getpeso(List<String> parziale) {
		Integer peso=0;
		for(int i=1; i<parziale.size();i++)
		{
			String id1=parziale.get(i-1);
			String id2=parziale.get(i);
			peso+=(int) this.grafo.getEdgeWeight(this.grafo.getEdge(id1, id2));
		}
		return peso;
	}

	public int getArchi()
	{
		return this.grafo.edgeSet().size();
	}
	
	public int getVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public List<String> getCategorieReato()
	{
		return this.dao.listCategorie();
	}
	public List<Date> getDate()
	{
		return this.dao.listDate();
	}
}
