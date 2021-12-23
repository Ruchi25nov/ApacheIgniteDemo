package com.xoriant.service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;

import org.apache.ignite.cache.store.CacheStore;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.SpringResource;
import org.jetbrains.annotations.Nullable;




public class PersonStore implements CacheStore<Long, Person> {

    @SpringResource(resourceName = "dataSource")
    private DataSource dataSource;

    // This method is called whenever IgniteCache.loadCache() method is called.
    @Override
    public void loadCache(IgniteBiInClosure<Long, Person> clo, @Nullable Object... objects) throws CacheLoaderException {
        System.out.println(">> Loading cache from store...");

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("select * from PERSON")) {
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        Person person = new Person(Long.valueOf(rs.getInt(1)),rs.getString(2));
                        System.out.println("===="+person.toString());
                       clo.apply(person.getId(), person);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new CacheLoaderException("Failed to load values from cache store.", e);
        }
    }

    
    
   	@Override
	public Person load(Long key) throws CacheLoaderException {
		 System.out.println(">> Loading person from store...");

	        try (Connection conn = dataSource.getConnection()) {
	            try (PreparedStatement st = conn.prepareStatement("select * from PERSON where id = ?")) {
	                st.setString(1, key.toString());

	                ResultSet rs = st.executeQuery();

	                return rs.next() ? new Person(rs.getInt(1), rs.getString(2)) : null;
	            }
	        }
	        catch (SQLException e) {
	            throw new CacheLoaderException("Failed to load values from cache store.", e);
	        }
	}



	@Override
	public Map<Long, Person> loadAll(Iterable<? extends Long> keys) throws CacheLoaderException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void write(Entry<? extends Long, ? extends Person> entry) throws CacheWriterException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void writeAll(Collection<Entry<? extends Long, ? extends Person>> entries) throws CacheWriterException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void delete(Object key) throws CacheWriterException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void deleteAll(Collection<?> keys) throws CacheWriterException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void sessionEnd(boolean commit) throws CacheWriterException {
		// TODO Auto-generated method stub
		
	}



	}