/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment4;

import Assignment4.GraphEdge;
import Assignment4.Graph;
import Assignment4.GraphNode;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 *
 * @author Jesse
 */
public class GraphMiniMapView extends Canvas {
    private double wWidth;
    private double wHeight;
    //Canvas mapCan;
    private Graph mapGraph; 
    private GraphicsContext graphics;
    private double hMapBounds = wHeight - 20;
    private double wMapBounds = wWidth - 20;

   public GraphMiniMapView(int Width, int Height,Graph myGraph)
    {
        
        this.setHeight(Height);
        this.setWidth(Width);
        wHeight = this.getHeight();
        wWidth = this.getWidth();
        mapGraph = myGraph;
        graphics = this.getGraphicsContext2D();
        graphics.setFill(Color.BLACK);
        graphics.setLineWidth(1); 
        //graphics.strokeRect(0,0,wWidth,wHeight);
       //initial draw
        this.drawNodes(graphics);
        this.drawEdges(graphics);
        
        //NOTE: I wanted to use these functions for everything, but for some reason it wouldn't work. Seems to be fixed after all this, though.
      
        //Node Checks. As nodes are generally tied to updates, the nodes check for updates, as well as deletions and additions.
       mapGraph.getNodes().addListener(new ListChangeListener<GraphNode>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends GraphNode> change) {
                while (change.next()) {
                    if (change.wasRemoved()){
                        //  IF we remove something, the easiest way to do it is to clear everything and redraw.
                        graphics.clearRect(0,0, wWidth, wHeight);
                        drawEdges(graphics);
                        drawNodes(graphics);
                    } else if (change.wasUpdated()){
                        //if one of the nodes has an update in its position, the edges have to be updated as well because they could have moved too.
                        //We also have to clear everything before hand or it looks like a mess.
                         graphics.clearRect(0,0, wWidth, wHeight);
                            drawEdges(graphics);
                            drawNodes(graphics);
                           
                        } else {
                        //othherwise (ie: on adding), just redraw the nodes. It's the best way to deal with it.
                        //don't want to delete the previous stuff in this case as we've just added a new node.
                            drawNodes(graphics);

                       
                    }
                }
            }
       
               
    });
       //Edge Checks. Since the edges can only be added or removed TO existing nodes, and cannot be moved unless a node is moving, we only have to
       //check for addition and removal of edges.
        mapGraph.getEdges().addListener(new ListChangeListener<GraphEdge>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends GraphEdge> c) {
                while (c.next()) {
                    } if (c.wasRemoved()) {
                        //if a edge was removed, we need to again, clear and redraw. note that this probably isn't the best way to do it (as you can tell during runtime
                        //BUT IT WORKS.
                        graphics.clearRect(0,0, wWidth, wHeight);
                        drawNodes(graphics);
                        drawEdges(graphics);
                    } else {
                        //if we add an edge, best to just draw the edge.
                        drawEdges(graphics);
                       
                    }
                }
            
        });
    }
   private void drawEdges(GraphicsContext gc){
    
    gc.setLineWidth(4); 
        for (GraphEdge e : mapGraph.getEdges())
        {
            
            gc.strokeLine(e.getN1().getX()*wWidth,e.getN1().getY()*wHeight,e.getN2().getX()*wWidth,e.getN2().getY()*wHeight);
            
        }
}    
private void drawNodes(GraphicsContext gc){
        for (GraphNode v : mapGraph.getNodes())
        {
            gc.fillOval(v.getX()*wWidth,v.getY()*wHeight,10,10);
        }
            //System.out.println(v.getX()*wWidth);
            //System.out.println(v.getY()*wHeight);
            //graphics.fillOval(v.getX()/wWidth,v.getY()/wHeight,3,3);
            //addVertex(v);
        
       //graphics = this.getGraphicsContext2D();
       
       //graphics.strokeLine(10,10, 50, 50);
       //graphics.strokeRect(0,0,wWidth,wHeight);
    
    }
}

  

     //TODO: Need to still add a listener that redraws this whenever the graph is updated.
     //Need oneor two listener(s) here in the constructor which just calls the redraw method

   
