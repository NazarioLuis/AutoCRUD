package py.nl.AutoCrud.dao;

import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.query.Query;

public class AutoDAO<T> extends GenericDAO<T>{

	public AutoDAO(Class<T> clazz) {
		super(clazz);
	}
	

	public List<T> filter(String filter, List<Field> attributes) {
		openTransaction();
		String where = "";
		for (int i = 0; i < attributes.size(); i++) {
			if(where.isEmpty()) where = "where upper("+attributes.get(i).getName()+") like :filtro ";
			else where += "or upper("+attributes.get(i).getName()+") like :filtro ";
		}
		
		Query<T> query = getSession().createQuery(
				"from "+clazz.getName()
				+" "+where
				+" order by id desc",
				clazz
		);
		
		query.setParameter("filtro", "%"+filter.toUpperCase()+"%");
		List<T> lista = query.getResultList();
		commit();
		return lista;
	}


	
}
