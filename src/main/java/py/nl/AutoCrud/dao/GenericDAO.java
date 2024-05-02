package py.nl.AutoCrud.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import py.nl.AutoCrud.util.ConnectionHelper;

public abstract class GenericDAO <T>{
	protected Class<T> clazz;
	
	public GenericDAO(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public void save(T entity) throws Exception {
		openTransaction();
		try {
			getSession().merge(entity);
			commit();
		} catch (Exception e) {
			rollback();
			throw e;
		}
	}
	
	public void remove(T entity) throws Exception{
		openTransaction();
		try {
			getSession().remove(entity);
			commit();
		} catch (Exception e) {
			rollback();
			throw e;
		}
	}
	
	public int nextId(){
		openTransaction();
		Query<Integer> query = getSession().createQuery("select max(id) from "+clazz.getName(),Integer.class);
		Integer result = query.list().get(0);
		if (result == null) {
			result = 0;
		}
		commit();
		return result+1;
	}
	
	public T getById(Serializable id){
		openTransaction();
		T result = getSession().get(clazz, id);
		commit();
		return result;
	}
	
	public List<T> getAll(){
		openTransaction();
		Query<T> query = getSession().createQuery("from "+clazz.getName()+" order by id", clazz);
		List<T> lista = query.getResultList();
		commit();
		return lista;
	}
		
	protected void commit(){
		getSession().flush();
		getSession().getTransaction().commit();
	}
	protected void rollback(){
		if(getSession().getTransaction().isActive())
			getSession().getTransaction().rollback();
	}
	
	protected void openTransaction(){
		if (!getSession().getTransaction().isActive()) getSession().beginTransaction();
	}

	protected Session getSession() {
		return ConnectionHelper.getSessionFactory().getCurrentSession();
	}
	
	
}
