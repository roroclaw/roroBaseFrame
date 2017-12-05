package com.roroclaw.base.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{
	private final static Logger  logger  = LoggerFactory.getLogger("DynamicDataSource");        
	
	@Override      
	protected Object determineCurrentLookupKey() {           
		 return DbSwitcher.getDbType();         
	}   
}
