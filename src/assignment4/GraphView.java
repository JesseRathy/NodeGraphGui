package Assignment4;

import java.util.HashMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class GraphView extends Pane {
    private final HashMap<GraphEdge, Line> displayEdges = new HashMap<>();
    private final HashMap<GraphNode, StackPane> displayNodes = new HashMap<>();
    private final Graph model;
    private final int width;
    private final int height;
    
    public GraphView(Graph g, int Width, int Height)
    {
        width = Width;
        height = Height;
        model = g;
        
        //Populate the view with the initial state of the graph
        for (GraphEdge e : g.getEdges())
        {
            addEdge(e);
        }
        for (GraphNode v : g.getNodes())
        {
            addVertex(v);
        }

        //Listen for added or removed edges
        g.getEdges().addListener(new ListChangeListener<GraphEdge>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends GraphEdge> c) {
                while (c.next()) {
                    if (c.wasPermutated()) {
                        //Order doesn't matter
                    } else if (c.wasUpdated()) {
                        //Updates are handled elsewhere, but this may be useful in other views
                    } else {
                        for (GraphEdge remitem : c.getRemoved()) {
                            removeEdge(remitem);
                        }
                        for (GraphEdge additem : c.getAddedSubList()) {
                            addEdge(additem);
                        }
                    }
                }
            }
        });
        
        //Listen for added or removed nodes
        g.getNodes().addListener(new ListChangeListener<GraphNode>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends GraphNode> change) {
                while (change.next()) {
                    if (change.wasPermutated()) {
                        //Order doesn't matter
                    } else if (change.wasUpdated()) {
                        //Updates are handled elsewhere, but this may be useful in other views
                    } else {
                        for (GraphNode remitem : change.getRemoved()) {
                            removeNode(remitem);
                        }
                        for (GraphNode additem : change.getAddedSubList()) {
                            addVertex(additem);
                        }
                    }
                }
            }
        });
        
        //Listen for mouse clicking on the pane itself, and add a new node to the model if it is
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                GraphNode v = new GraphNode(t.getX()/(double)width, t.getY()/(double)height);
                model.getNodes().add(v);
                select(v);
            }
        });
    }
    
    private void addVertex(final GraphNode n)
    {
        //Create a stackpane so that we can layer a label over a circle to represent the node
        final StackPane sp = new StackPane();
        //Size the stackpane to 1/15th of the width of the pane.  This works well for sizes around 1000x1000
        //but may not be particularly generalizable
        sp.setMaxSize(width/15, height/15);
        sp.setMinSize(width/15, height/15);
        
        //Create a circle for the node
        final Circle c = new Circle(Math.min(width/15, height/15));
        //Add the circle to the stackpane
        sp.getChildren().add(c);
        
        //Create a label for the node
        final Label l = new Label(n.getText());
        //Brown is an okay color for the text
        l.setTextFill(Color.BROWN);
        //Add the label to the stackpane
        //By adding it second it will appear atop the circle
        sp.getChildren().add(l);
        
        //Add the stackpane to the pane
        this.getChildren().add(sp);
        //Move the stackpane to the position specified by the model (scaled by the size of the pane)
        sp.relocate(width * n.getX(), height * n.getY());
        
        //Add the stackpane to the hashmap so that it can be found later
        displayNodes.put(n, sp);
        
        
        //When the circle is clicked, there are several possible outcomes relating to selection
        c.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //If the control key is down, the circle must be deleted
                if (t.isControlDown())
                {
                    deselect();
                    model.getNodes().remove(n); 
                }
                //If the selected node is same as the node which this circle represents, deselect this circle
                else if (selected == n)
                {
                    deselect();
                }
                //If shift is down and another node is selected, add an edge between them
                else if (t.isShiftDown() && selected != null)
                {
                    GraphEdge e = new GraphEdge(n, selected);
                    model.getEdges().add(e);
                }
                //If no modifier keys are down and the node is not already selected, simply select it
                else
                {
                    select(n);
                }
                //This event is handled.  Consume it so that it doesn't get passed on
                t.consume();
            }
        });
        
        //When the circle is dragged, change it's coordinates in the model
        c.onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                n.setX(t.getSceneX()/width);
                n.setY(t.getSceneY()/height);
                //Ensure no other nodes are selected
                deselect();
                //This event is handled.  Consume it so that it doesn't get passed on
                t.consume();
            }
        });

        
        //When the label is pressed, we need to make it editable
        l.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //To make the label editable, we replace it with a textfield
                
                //Remove the label from the stackpane
                sp.getChildren().remove(l);
                
                //Create a new textfield
                final TextField tf = new TextField();
                //Copy the text from the model into the textfield
                tf.setText(n.getText());
                
                //Add the textfield to the stackpane
                sp.getChildren().add(tf);
                
                //If the user pressed enter inside the textfield, replace it with the label
                tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t) {
                        if (t.getCode() == KeyCode.ENTER)
                        {
                            //remove the textfield
                            sp.getChildren().remove(tf);
                            //add the label back
                            sp.getChildren().add(l);
                            //Change the text in the model
                            if (!tf.getText().isEmpty())
                                n.setText(tf.getText());
                        }
                    }
                });
            }
        });
        
        //If the node's text changes in the model, change it in the view
        n.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                l.setText(n.getText());
            }
        });
        
        //If the node moves in teh model, move it in the view.
        //We can use the same handler for changes in the x and y coordinates
        ChangeListener<Number> moved = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                sp.relocate(width * n.getX(), height * n.getY());
            }
        };
        
        //Add the moved handler to the change events for the x and y coordinates of the node in the model
        n.xProperty().addListener(moved);
        n.yProperty().addListener(moved);
        
        
    }
    
    
    
    private void addEdge(final GraphEdge e)
    {
        //Create a new line whose coordinates are defined by the edge e
        //These are determined by the two nodes which represent the edge, and are scaled to the size of the pane
        final Line l = new Line(e.getN1().getX() * width, e.getN1().getY() * height, e.getN2().getX() * width, e.getN2().getY() * height);
        //5 seems to be an appropriate width
        l.setStrokeWidth(5);
        
        //Add the line to the pane
        this.getChildren().add(0,l);
        
        //Add the line to the hashmap so that is can be found later
        displayEdges.put(e, l);
        
        //When the user clicks on the line, remove the corresponding edge from the model
        l.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                model.getEdges().remove(e);
                t.consume();
            }
        });
        
        //When the edge moves in the model, redraw it's coordinates
        //It's easiest to create just one handler that updates all the coordinates of the line
        ChangeListener<Number> moved = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                
                l.setStartX(e.getN1().getX() * width);
                l.setStartY(e.getN1().getY() * height);
                l.setEndX(e.getN2().getX() * width);
                l.setEndY(e.getN2().getY() * height);
            }
        };

        //Add the event handler to the appropriate change events:
        //This is the x and y properties of both nodes representing the edge
        e.getN1().xProperty().addListener(moved);
        e.getN1().yProperty().addListener(moved);
        e.getN2().xProperty().addListener(moved);
        e.getN2().yProperty().addListener(moved);
    }
    
    private void removeNode(GraphNode n)
    {
        //If the item to be deleted is the selected item, deselect it
        if (selected == n)
        {
            selected = null;
        }
        //Use the hashmap to get the stackpane from the view which represents the node n from the model
        StackPane toRemove = displayNodes.get(n);
        //Remove the stackpane from the pane
        this.getChildren().remove(toRemove);
    }
    
    private void removeEdge(GraphEdge e)
    {
        //Use the hashmap to get the line from the view which represents the edge e from the model
        Line edgeToRemove = displayEdges.get(e);
        //Remove the edge from the pane
        this.getChildren().remove(edgeToRemove);
    }
    
    
    //Keep track of the node which is currently selected
    private GraphNode selected = null;

    //A method to deselect the currently selected node, if there is one
    private void deselect()
    {
        if (selected != null)
        {
            //Use the hashmap to get the stackpane from the view which represents the selected node from the model
            StackPane currSelectedStackPane = displayNodes.get(selected);
            //Get the circle from the stackpane
            Circle currSelected = (Circle)currSelectedStackPane.getChildren().get(0);
            //Set the circle's color to black (the color for deselected nodes)
            currSelected.setFill(Color.BLACK);
            //Mark the selected node as null; nothing is selected
            selected = null;
        }
    }
    
    private void select(GraphNode n)
    { 
        //Ensure that no other nodes are selected
        deselect();
        
        //Use the hashmap to get the stackpane from the view which represents the node n from the model
        StackPane currSelectedStackPane = displayNodes.get(n);
        //Get the circle from the stackpane
        Circle currSelected = (Circle)currSelectedStackPane.getChildren().get(0);
        //Set the circle's color to gray (the color for selected nodes)
        currSelected.setFill(Color.DARKGRAY);
        //Mark the selected node as n
        selected = n;
    }
}
