package Assignment4;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class MVCEditor extends Application {
    
    //@Override
    GridPane testPane = new GridPane();
    //= new GridPane();
    public void start(Stage primaryStage) {
        //testPane = new GridPane();
        Graph g = new Graph();
        GraphNode n1 = new GraphNode(.25, .25);
        GraphNode n2 = new GraphNode(.75, .75);
        g.getNodes().add(n1);
        g.getNodes().add(n2);
        GraphEdge e = new GraphEdge(n1, n2);
        g.getEdges().add(e);
        
        GraphView gv = new GraphView(g, 1000, 1000);
        gv.setMinSize(1000,1000);
        gv.setMaxSize(1000,1000);
        
        GraphNodeViewList list = new GraphNodeViewList(500,700,g);
        GraphMiniMapView map = new GraphMiniMapView(500,500,g);
        VBox mapList = new VBox();
        mapList.setSpacing(5);
        //mapList.setAlignment(Pos.TOP_RIGHT);
        mapList.getChildren().addAll(map,list);
        testPane.add(gv,0,0);
        testPane.add(mapList,1,0);
        //testPane.add(map,1,0);
        //testPane.add(list,1,1);
       
        Scene scene = new Scene(testPane, 1500, 1500);
        
        
        primaryStage.setTitle("Assignment4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
