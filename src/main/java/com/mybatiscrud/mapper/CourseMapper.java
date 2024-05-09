package com.mybatiscrud.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.mybatiscrud.model.Course;

@Mapper
public interface CourseMapper {
	
    Long count();

	
	//all lsit
    public List<Course> find(RowBounds rowBounds);
    
    //insert
    public void  insert(Course course);
    
	Long serachAccountTotal(Course course);
	
    public List<Course> search(Course course,RowBounds rowBounds);

    //edit
	List<Course> edit(int id);


	Course csv(int id);


	List<Course> paginationClick(@Param("name")String name,@Param("start_date")Date start_date,@Param("end_date")Date end_date,RowBounds rowBounds);
 

	Long paginationClickAccount(@Param("name") String name,@Param("start_date") Date start_date,@Param("end_date") Date end_date);


	Course csvTest(int id);



	







}
