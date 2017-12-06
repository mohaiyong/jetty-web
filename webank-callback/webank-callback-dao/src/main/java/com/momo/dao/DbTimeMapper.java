package com.momo.dao;

import org.springframework.stereotype.Repository;

@Repository("dbtimeMapper")
public interface DbTimeMapper {

	public String querySystemTime();

	public String querySystemDate();
}