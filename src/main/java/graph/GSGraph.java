package graph;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import com.google.common.eventbus.Subscribe;

import utilities.GSEventBus;
import utilities.events.ClearGraphEvent;
import utilities.events.GraphInitializedEvent;
import utilities.events.NodeReachedEvent;
import utilities.events.NodeRemovedEvent;
import utilities.events.NodeTypeSwitchEvent;

public class GSGraph<T> {
	private final Graph graph;
	private T root;
	private final GSEventBus<T> eventSupplier;
	
	private final ConcurrentMap<T, Node> ext2intNodeMap = new ConcurrentHashMap<>();
	private final ConcurrentMap<Node, T> int2extNodeMap = new ConcurrentHashMap<>();
	
	public GSGraph(GSEventBus<T> e){
		this.eventSupplier = e;
		this.eventSupplier.register(this);
		this.graph = new SingleGraph("Graph Stream");
		this.graph.setStrict(false);
		this.graph.setAutoCreate(true);
	}
	public GSGraph(String s, GSEventBus<T> e){
		this.eventSupplier = e;
		this.eventSupplier.register(this);
		this.graph = new SingleGraph(s);
		this.graph.setStrict(false);
		this.graph.setAutoCreate(true);
	}
	
	public Graph get_graph() {
		return graph;
	}
	
	public T get_node(String n) {
		Node nodeInt = this.graph.getNode(n);
		if (this.int2extNodeMap.containsKey(nodeInt))
			return this.int2extNodeMap.get(nodeInt);
		else return null;
	}
	
	protected synchronized Node newNode(final T newNodeExt) {

		/* create new node */
		final String nodeId = newNodeExt.toString();
		if (this.ext2intNodeMap.containsKey(newNodeExt) || this.graph.getNode(nodeId) != null) {
			throw new IllegalArgumentException("Cannot insert node " + newNodeExt + " because it is already known.");
		}
		final Node newNodeInt = this.graph.addNode(nodeId);
		this.graph.getNode(nodeId).setAttribute("ui.label", nodeId);
		this.graph.getNode(nodeId).setAttribute("ui.style", "fill-color: gray;");

		/* store relation between node in graph and internal representation of the node */
		this.ext2intNodeMap.put(newNodeExt, newNodeInt);
		this.int2extNodeMap.put(newNodeInt, newNodeExt);

		/* store relation between node and parent in internal model */
		return newNodeInt;
	}
	
	protected synchronized Edge newEdge(final T from, final T to) {
		/* check if the nodes in the graph */
		final Node fromInt = this.ext2intNodeMap.get(from);
		final Node toInt = this.ext2intNodeMap.get(to);
		if (fromInt == null)
			throw new IllegalArgumentException("Cannot insert edge between " + from + " and " + to + " since node " + from + " does not exist.");
		if (toInt == null)
			throw new IllegalArgumentException("Cannot insert edge between " + from + " and " + to + " since node " + to + " does not exist.");
		/* create the edge */
		final String edgeId = fromInt.getId() + "-" + toInt.getId();
		return this.graph.addEdge(edgeId, fromInt, toInt, true);
	}
	
	protected synchronized void addNode(final T from, final T to) {
		try {
			if (!ext2intNodeMap.containsKey(to))
				newNode(to);
			newEdge(from, to);
		} catch (Exception ex) {
			this.eventSupplier.unregister(this);
			this.graph.clear();
		}
	}
	
	protected synchronized void removeNode(final T newNodeExt) {
		try {
			this.graph.removeNode(ext2intNodeMap.get(newNodeExt));
		} catch (Exception ex) {
			this.eventSupplier.unregister(this);
			this.graph.clear();
		}
	}
	
	protected synchronized void switchType(final T nodeExt, String t) {
		try {
			if (!ext2intNodeMap.containsKey(nodeExt))
				throw new NoSuchElementException("Cannot switch type of node " + nodeExt + ". This node does not excist.");
			String type = "fill-color: gray;";
			switch (t) {
			case "root":{
				type = "size: 20px; fill-color: black;";
				break;
			}
			case "deadend":{
				type = "fill-color: red;";
				break;
			}
			case "maxdepth":{
				type = "fill-color: purple;";
				break;
			}
			case "open":{
				type = "fill-color: blue;";
				break;
			}
			case "closed":{
				type = "fill-color: cyan;";
				break;
			}
			case "solution":{
				type = "size: 20px; shape: box; fill-color: green;";
				break;
			}
			}
			ext2intNodeMap.get(nodeExt).setAttribute("ui.style", type);
		} catch (Exception ex) {
			this.eventSupplier.unregister(this);
			this.graph.clear();
		}
	}
	
	@Subscribe
	public synchronized void receiveGraphInitEvent(GraphInitializedEvent<T> e) {
		try {
			if (root != null)
				throw new UnsupportedOperationException("Cannot initialize the graph for a second time!");
			root = e.getRoot();
			if (root == null)
				throw new IllegalArgumentException("Root must not be NULL");
			this.newNode(root);
			this.switchType(root,"root");
		} catch (Exception ex) {
			this.eventSupplier.unregister(this);
			this.graph.clear();
		}
	}
	
	@Subscribe
	public synchronized void receiveNewNodeEvent(NodeReachedEvent<T> e) {
		this.addNode(e.getParent(), e.getNode());
	}
	
	@Subscribe
	public synchronized void receiveNodeRemovedEvent(NodeRemovedEvent<T> e) {
		this.removeNode(e.getNode());
	}
	
	@Subscribe
	public synchronized void receiveNodeTypeSwitchEvent(NodeTypeSwitchEvent<T> e) {
		if (e.getNode() == root)
			return;
		else {
			this.switchType(e.getNode(), e.getType());
		}
	}
	
	@Subscribe
	public synchronized void receiveCloseEvent(ClearGraphEvent<T> e) {
		this.eventSupplier.unregister(this);
		this.graph.clear();
	}
}
