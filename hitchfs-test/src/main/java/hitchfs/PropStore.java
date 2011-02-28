package hitchfs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.collect.Maps;

public class PropStore {

	Map<Class<? extends FileProp>, FileProp> props = Maps.newHashMap();

	public PropStore withProperty(FileProp property) {
		checkNotNull(property);
		this.props.put(property.getClass(), property);
		return this;
	}
	
	public <T extends FileProp> PropStore withProperty(Class<T> type, T value) {
		checkNotNull(type);
		this.props.put(type, value);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends FileProp> T getProperty(Class<T> prop) {
		return (T) this.props.get(prop);
	}

	public boolean hasProperty(Class<?> type) {
		return props.containsKey(type);
	}
	
	public <P extends FileProp, S> S visitProperty(Class<P> type, PropVisitor<P, S> visitor) {
		P p = getProperty(type);
		if (p != null) {
			return visitor.some(p);
		}
		return visitor.none(this);
	}
	
	public Map<Class<? extends FileProp>, FileProp> getMap() {
		return this.props;
	}
	
	public Map<Class<? extends FileProp>, FileProp> setMap(
			Map<Class<? extends FileProp>, FileProp> props) {
		Map<Class<? extends FileProp>, FileProp> old = this.props;
		this.props = props;
		return old;
	}

	public static interface PropVisitor<P, S> {
		public S some(P p);
		public S none(PropStore props);
	}

	
}
