package hitchfs;

import java.util.Map;

import com.google.common.collect.Maps;

public class PropStore {

	Map<Class<? extends FileProp>, FileProp> props = Maps.newHashMap();

	public PropStore withProperty(FileProp property) {
		this.props.put(property.getClass(), property);
		return this;
	}
	
	public <T extends FileProp> PropStore withProperty(Class<T> type, T value) {
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
	
}
