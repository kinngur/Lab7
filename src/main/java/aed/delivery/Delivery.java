package aed.delivery;

import es.upm.aedlib.Position;
import es.upm.aedlib.graph.*;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;

import java.util.ArrayList;
import java.util.Iterator;

public class Delivery<V> {

  DirectedGraph<V, Integer> graph = new DirectedAdjacencyListGraph<V, Integer>();
  ArrayList<Vertex<V>> vertexList = new ArrayList<>();

  // Construct a graph out of a series of vertices and an adjacency matrix.
  // There are 'len' vertices. A null means no connection. A non-negative
  // number represents distance between nodes.
  public Delivery(V[] places, Integer[][] gmat) {
    if(places.length == 0) return;
    for (V place : places) { //inserto todos los vertices y los añado a vertexList
      vertexList.add(graph.insertVertex(place));
    }
    for (int i = 0; i < places.length; i++) {//recorro cada elemento de la matriz gmat
      for (int j = 0; j < places.length; j++) {
        if(gmat[i][j] != null) {//si hay un camino de i a j le enchufo una arista entre ambos nodos
          graph.insertDirectedEdge(vertexList.get(i), vertexList.get(j), gmat[i][j]);
        }
      }
    }
  }

  // Just return the graph that was constructed
  public DirectedGraph<V, Integer> getGraph() {
    return graph;
  }

  // Return a Hamiltonian path for the stored graph, or null if there is none.
  // The list containts a series of vertices, with no repetitions (even if the path
  // can be expanded to a cycle).
  public PositionList <Vertex<V>> tour() {
    if (!graph.isEmpty()) {
      PositionList<Vertex<V>> path = new NodePositionList<>();
      for (Vertex<V> vertice : vertexList) {
        path.addLast(vertice);
        if (hamiltonianPath(vertice, path)) {
          return path;
        }
        path.remove(path.last());
      }
    }
    return null;
  }

  private boolean hamiltonianPath(Vertex<V> verticeActual, PositionList<Vertex<V>> path){
    if (path.size() == vertexList.size()) {
      return true;
    }
    for (Edge<Integer> edge : graph.outgoingEdges(verticeActual)){
      Vertex<V> vecino = graph.endVertex(edge);
      boolean visitado = false;
      Iterator<Vertex<V>> it = path.iterator();
      while (it.hasNext() && !visitado){
        if (it.next().equals(vecino)) visitado = true;
      }
      if (!visitado){
        path.addLast(vecino);
        if (hamiltonianPath(vecino, path)){
          return true;
        }
        path.remove(path.last());
      }
    }
    return false;
    }

  // Return the length of a path in the graph.
  // The path has to have at least one node.
  public int length(PositionList<Vertex<V>> path) {
    int longTotal = 0;
    if (!path.isEmpty()) {
      Position<Vertex<V>> pos = path.first();
      while (path.next(pos) != null) {
        Vertex<V> vertice = pos.element();
        for (Edge<Integer> edge : graph.outgoingEdges(vertice)) {
          if (graph.endVertex(edge).equals(path.next(pos).element())) {
            longTotal += edge.element();
          }
        }
        pos = path.next(pos);
      }
    }
    return longTotal;
  }

  public String toString() {
    return "Delivery";
  }
}