package utilities.events;

public class NodeReachedEvent<T> {

	private final T parent, node;
	private final String type;

	public NodeReachedEvent(T parent, T node, String type) {
		super();
		this.parent = parent;
		this.node = node;
		this.type = type;
	}

	public T getParent() {
		return parent;
	}

	public T getNode() {
		return node;
	}

	public String getType() {
		return type;
	}

}
