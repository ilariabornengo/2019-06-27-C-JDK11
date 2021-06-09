/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Date> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo...\n");
    	Date giorno=this.boxGiorno.getValue();
    	String categoria=this.boxCategoria.getValue();
    	this.model.creaGrafo(categoria, giorno);
    	this.txtResult.appendText("GRAFO CREATO!!!\n");
    	this.txtResult.appendText("# archi: "+this.model.getArchi()+"\n");
    	this.txtResult.appendText("#vertici: "+this.model.getVertici()+"\n");
    	double pesoMedio=this.model.getPesoMediano();
    	txtResult.appendText("IL PESO MEDIANO E': "+pesoMedio+"\n");
    	txtResult.appendText("GLI ARCHI CON PESO MINORE SONO:\n");
    	List<Adiacenza> adiacenzeOK=new ArrayList<>(this.model.getOrdinati(giorno, categoria, pesoMedio));
    	for(Adiacenza a:adiacenzeOK)
    	{
    		txtResult.appendText(a.toString()+"\n");
    	}
    	this.boxArco.getItems().addAll(adiacenzeOK);
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso...\n");
    	Adiacenza a=this.boxArco.getValue();
    	List<String> percorsoMax=this.model.percorsoMassimo(a);
    	txtResult.appendText("PERCORSO DI PESO MAX PER ANDARE DA "+a.getId1()+" A "+a.getId2()+"\n");
    	for(String s:percorsoMax)
    	{
    		txtResult.appendText(s+"\n");
    	}
    	txtResult.appendText("IL PESO MASSIMO VALE: "+this.model.pesoTop);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(this.model.getCategorieReato());
    	this.boxGiorno.getItems().addAll(this.model.getDate());
    }
}
