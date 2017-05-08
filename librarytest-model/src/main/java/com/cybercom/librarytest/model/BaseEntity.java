package com.cybercom.librarytest.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base class for entities in the Library Test application.
 * @author lemor1
 *
 */
@MappedSuperclass
public class BaseEntity {
	@Id
	@GeneratedValue
	protected Long id;
	
	public BaseEntity() {}
	
	public BaseEntity(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
