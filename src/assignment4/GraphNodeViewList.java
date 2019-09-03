/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment4;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Assignment4.GraphNode;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
/**
 *
 * @author Jesse
 */
  
//need a graph here?
public class GraphNodeViewList extends VBox {
  private int wHeight;
  private int wWidth;
  private TextField textChanger; 
  private ListView  nodeList;
  private Graph ourGraph;
  private GraphNode selectedNode;
  //private ObservableList<GraphNode> nodeSet = FXCollections.observableArrayList();
  protected ListProperty<GraphNode> listProperty = new SimpleListProperty<>();
  //protected ListProperty<GraphNode> listProperty2 = new SimpleListProperty<>();
  
  public GraphNodeViewList(int height, int width, Graph g){
      wHeight = height;
      wWidth = width;
      textChanger = new TextField();
      nodeList = new ListView();
      //nodeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      ourGraph = g;
      this.setUp();
       nodeList.getSelectionModel().selectedItemProperty().addListener(
               new ChangeListener() {
          @Override
          public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            if (!nodeList.getSelectionModel().isEmpty()){  
            int ourIndex = ourGraph.getNodes().indexOf(nodeList.getSelectionModel().getSelectedItem());
            String nodeText = ourGraph.getNodes().get(ourIndex).getText();
            textChanger.setText(nodeText);
            }
           }
                   
            });
           
        textChanger.setOnKeyPressed(new EventHandler<KeyEvent>(){
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode().equals(KeyCode.ENTER))
            {
               if(nodeList.getSelectionModel().isEmpty()){
            
               } else {
                //selectedNode.setText(textChanger.getText());
          int ourIndex = ourGraph.getNodes().indexOf(nodeList.getSelectionModel().getSelectedItem());
               ourGraph.getNodes().get(ourIndex).setText(textChanger.getText());
               nodeList.getSelectionModel().clearSelection();
               }
               
          }
        }   
        });
               
       }
  
  private void setUp() {
      listProperty.set(ourGraph.getNodes());
      //textProperty.set(nodeList.itemsProperty());
      //listProperty2.set(textChanger.);
      nodeList.itemsProperty().bindBidirectional(listProperty);
      //textChanger.textProperty().bind(nodeList.itemsProperty());
      //nodeList.itemsProperty().bind(listProperty);
      //nodeList.itemsProperty().bindnodeList.itemsProperty().asString());
      this.getChildren().addAll(textChanger,nodeList);
    }
//   
//  private void changeSelectedNode(){
//      textChanger.getText();
 // private void changeSelectedNode(GraphNode n){
 //      textChanger.textProperty().addListener(new ChangeListener <String>(){  
 //       @Override
 //       public void changed(ObservableValue<? extends String> observable,
 //           String oldValue, String newValue) {
            
            
            //nodeList.itemsProperty().bind(listProperty.);
 //       }     
 //       });
      
 // }
  
  //this.getChildren().addAll(textChanger,nodeList);
    //nodeList.getSelectionModel().setSelectionNode(SelectionMode.SINGLE);
    //lv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
     //for (GraphNode v : ourGraph.getNodes()){
         
    //super.getChildren().add();
}
//to use this.getChildren need to make a 'set-up' method or use the constructor.
//Should be able to bind the obbserbablelist of nodes, should not need a  listener BUTi may possibly need one? if i do it's in the constuctor as well.
//anytime in the break 
