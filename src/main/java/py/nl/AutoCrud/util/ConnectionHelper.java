package py.nl.AutoCrud.util;

import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import jakarta.persistence.Entity;

public class ConnectionHelper {

	private static SessionFactory sessionFactory;
	private static String mappingPackages;

	static {
		Configuration configuration = new Configuration();
		mappingPackages = configuration.getProperty("mapping_packages");
		StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		MetadataSources metadataSources = addMapping(new MetadataSources(serviceRegistry));
		try {
			sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
			StandardServiceRegistryBuilder.destroy(serviceRegistry);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	private static MetadataSources addMapping(MetadataSources ms) {
		String[] pkgs = mappingPackages.split(",");
		
		for (int i = 0; i < pkgs.length; i++) {
			Reflections reflections = new Reflections(pkgs[i]);
			Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Entity.class);

			for(Class<?> clazz : classes)
			{
				ms.addAnnotatedClass(clazz);
			}
		}
		return ms;
	}

}
