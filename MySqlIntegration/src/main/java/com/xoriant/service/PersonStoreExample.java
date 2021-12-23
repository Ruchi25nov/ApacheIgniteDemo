package com.xoriant.service;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;

public class PersonStoreExample {
	public static void main(String[] args) throws IgniteException {
		Ignition.setClientMode(true);

		try (Ignite ignite = Ignition.start("C:\\apache-ignite-2.11.0-bin\\config\\default-config.xml")) {
			try (IgniteCache<Long, Person> cache = ignite.getOrCreateCache("personCache")) {
				// Load cache with data from the database.
				cache.loadCache(null);

				// Execute query on cache.
				QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery("select id, name from Person"));

				System.out.println("data====");
				System.out.println(cursor.getAll());
			}
		}
	}
}
