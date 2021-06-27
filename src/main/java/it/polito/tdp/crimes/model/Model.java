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
	
	EventsDao dao;
	List<String> vertici;
	Graph<String,DefaultWeightedEdge> grafo;
	List<String> percorsoBest;
	public int pesoBest;
	
	public Model()
	{
		this.dao=new EventsDao();
	}
	
	public void creaGrafo(Date data,String categoria)
	{
		this.vertici=new ArrayList<String>();
		this.grafo=new SimpleWeightedGraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		this.dao.getVertici(vertici, data, categoria);
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//aggiungo gli archi
		for(Adiacenza a:this.dao.getAdiacenze(vertici, data, categoria))
		{
			if(this.grafo.vertexSet().contains(a.getS1()) && this.grafo.vertexSet().contains(a.getS2()))
			{
				Graphs.addEdge(this.grafo, a.getS1(), a.getS2(), a.getPeso());
			}
		}
	}
	public List<Adiacenza> getArchi(Date data,String categoria)
	{
		return this.dao.getAdiacenze(vertici, data, categoria);
	}
	public double getpesoMediano()
	{
		double minimo=Double.MAX_VALUE;
		double massimo=0.0;
		double finale=0.0;
		for(DefaultWeightedEdge d:this.grafo.edgeSet())
		{
			double peso=this.grafo.getEdgeWeight(d);
			if(peso>massimo)
			{
				massimo=peso;
			}else if(peso<minimo)
			{
				minimo=peso;
			}
		}
		
		finale=(massimo+minimo)/2;
		return finale;
	}
	public List<Adiacenza> getInferioreMediano(double pesoMediano,Date data,String categoria)
	{
		List<Adiacenza> infOK=new ArrayList<Adiacenza>();
		for(Adiacenza a:this.dao.getAdiacenze(vertici, data, categoria))
		{
			if(a.getPeso()<pesoMediano)
			{
				infOK.add(a);
			}
		}
		return infOK;
	}
	
	public List<String> getListaBest(Adiacenza a)
	{
		String partenza=a.getS1();
		String arrivo=a.getS2();
		this.percorsoBest=new ArrayList<String>();
		this.pesoBest=0;
		List<String> parziale=new ArrayList<String>();
		parziale.add(partenza);
		ricorsione(parziale,arrivo);
		return this.percorsoBest;
		
	}
	
	private void ricorsione(List<String> parziale, String arrivo) {
		String ultimo=parziale.get(parziale.size()-1);
		if(ultimo.equals(arrivo))
		{
			int pesoCompl=calcolaPeso(parziale);
			if(pesoCompl>this.pesoBest)
			{
				this.percorsoBest=new ArrayList<String>(parziale);
				this.pesoBest=pesoCompl;
				return;
			}
		}
		
		//fuori dal caso terminale
		for(String s:Graphs.neighborListOf(this.grafo, ultimo))
		{
			if(!parziale.contains(s))
			{
				parziale.add(s);
				ricorsione(parziale,arrivo);
				parziale.remove(s);
			}
		}
	}

	private int calcolaPeso(List<String> parziale) {
		int tot=0;
		for(int i=1;i<parziale.size();i++)
		{
			String v1=parziale.get(i-1);
			String v2=parziale.get(i);
			int peso=(int) this.grafo.getEdgeWeight(this.grafo.getEdge(v1, v2));
			tot+=peso;
		}
		return tot;
	}

	public int getVertici()
	{
		return this.grafo.vertexSet().size();
	}
	public int getArchi()
	{
		return this.grafo.edgeSet().size();
	}
	
	public List<String> categorie()
	{
		return dao.getCategorie();
	}
	public List<Date> data()
	{
		return dao.getDateOK();
	}
}
