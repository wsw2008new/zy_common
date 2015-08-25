package com.zy.common.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.zy.common.dao.CommonDao;

public abstract class CommonService<E,ID extends Serializable> {

	protected CommonDao<E,ID> commonDao;

	public void setCommonDao(CommonDao<E, ID> commonDao) {
		this.commonDao = commonDao;
	}

	public CommonDao<E, ID> getCommonDao() {
		return commonDao;
	}
	
	public E get(ID id) {
		return commonDao.getOne(id);
	}

	public E find(ID id) {
		return commonDao.findOne(id);
	}

	public List<E> getAll() {
		return commonDao.findAll();
	}
	
	public Long getTotalCount() {
		return commonDao.count();
	}

	public E save(E entity) {
		return commonDao.save(entity);
	}

	public List<E> save(Collection<E> entities){
		return commonDao.save(entities);
	}
	
	public E update(E entity) {
		return commonDao.save(entity);
	}
	
	public void delete(E entity) {
		commonDao.delete(entity);
	}

	public void delete(ID id) {
		commonDao.delete(id);
	}

	public void delete(Collection<E> entities) {
		commonDao.delete(entities);
	}

	public void flush() {
		commonDao.flush();
	}
	
	public List<E> findAll(Specification<E> spec) {
		return commonDao.findAll(spec);
	}
	
	public Page<E> findAll(Pageable pageable){
		return commonDao.findAll(pageable);
	}
	
	public Page<E> findAll(Specification<E> spec, Pageable pageable) {
		return commonDao.findAll(spec, pageable);
	}
	
	public List<E> findAll(Specification<E> spec, Sort sort) {
		return commonDao.findAll(spec);
	}
	
	public List<E> findAll(Iterable<ID> ids){
		return commonDao.findAll(ids);
	}
	
	public long count(Specification<E> spec) {
		return commonDao.count(spec);
	}
	
	/**
	 * 
	 * @param e
	 * @param id
	 * @param columns
	 * @return
	 */
	public E update(E e, ID id, String[] columns){
		
		if(columns == null){
			return this.update(e);
		}else{
			E os = this.find(id);
			try{
			Class<?> cl = e.getClass();
			Method[] methods = cl.getMethods();
			for(Field field : cl.getDeclaredFields()){
				String filedName = field.getName();
				filedName = filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
				for(String col : columns){
					if(col.equalsIgnoreCase(filedName)){
						//getֵ
						Method mGet = cl.getMethod("get" + filedName);
						Object val = mGet.invoke(e);
						//set
						for(Method m : methods){
							if(m.getName().equals("set" + filedName)){
								m.invoke(os, val);
								break;
							}
						}
						break;
					}
				}
			}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			//
			this.update(os);
			return os;
		}
	}
	
}
