package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	//txtResult.appendText("Calcola artisti connessi");
    	List<Adiacenza> lista= this.model.getAdiacenze();
    	if(lista==null) {
    		txtResult.appendText("Crea grafo");
    		return;	
    	}
    	Collections.sort(lista);
    	for(Adiacenza a:lista) {
    		txtResult.appendText(String.format("(%d,%d)=%d\n",a.getId1(), a.getId2(), a.getPeso()));
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	//txtResult.appendText("Calcola percorso");
    	Integer id;
    	try{
    		id=Integer.parseInt(txtArtista.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("Inserire id nel formato corretto");
    		return;
    	}
    	if(!this.model.grafoContiene(id)) {
    		txtResult.appendText("l'artista non è nel grafo");
    		return;
    	}
    	List<Integer> percorso=this.model.trovaPercorso(id);
    	txtResult.appendText("Percorso piu lungo:" +percorso.size()+"\n");
    	for(Integer v:percorso) {
    		txtResult.appendText(v+" ");
    	}
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	//txtResult.appendText("Crea grafo");
    	String r=boxRuolo.getValue();
    	if(r==null) {
    		txtResult.appendText("seleziona un ruolo");
    		return;
    	}
    	model.creaGrafo(r);
    	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi", this.model.vertici(), this.model.archi()));
    	btnCalcolaPercorso.setDisable(false);
    }

    public void setModel(Model model) {
    	this.model = model;
    	btnCalcolaPercorso.setDisable(true);
    	boxRuolo.getItems().addAll(model.getRuoli());
    	
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";
        
    }
}
