package net.freecraft.util;

import java.util.ArrayList;
import java.util.List;

public class Registry<T extends IRegistryEntry> {
	private Object[] objs = new Object[65536];
	private List<T> registered = new ArrayList<T>();
	public void register(int rawId, T object) {
		object.setId(rawId);
		registered.add(object);
		objs[rawId] = object;
	}
	@SuppressWarnings("unchecked")
	public T get(int rawId) {
		return (T)objs[rawId];
	}
	public List<T> getAll() {
		return registered;
	}
	public void bind(IRegistryEntry other, T obj) {
		register(other.getId(), obj);
	}
	@SuppressWarnings("unchecked")
	public T get(IRegistryEntry other) {
		return (T)objs[other.getId()];
	}
	public void clear() {
		objs = new Object[65536];
		registered = new ArrayList<T>();
	}
}
