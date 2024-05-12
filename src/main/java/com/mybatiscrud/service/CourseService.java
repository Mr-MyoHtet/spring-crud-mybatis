package com.mybatiscrud.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mybatiscrud.mapper.CourseMapper;
import com.mybatiscrud.model.Course;

@Service
public class CourseService {

	@Autowired
	CourseMapper mapper;

	// select件
	public Page<Course> getList(Pageable pageable) {
		RowBounds rowBounds = new RowBounds((int) pageable.getOffset(), pageable.getPageSize());
		List<Course> course = mapper.find(rowBounds);
		Long total = mapper.count();
		return new PageImpl<>(course, pageable, total);
		// return mapper.find();
	}

	// insert
	public void insert(Course course) {
		mapper.insert(course);
	}

	// serach
	public Page<Course> search(Course course, Pageable pageable) {
		List<Course> search_course = mapper.search(course,
				new RowBounds((int) pageable.getOffset(), pageable.getPageSize()));
		Long searchTotal = mapper.serachAccountTotal(course);
		return new PageImpl<>(search_course, pageable, searchTotal); //
	}

	public Course edit(int id) {
		return mapper.edit(id);
	}
	
	public void update(Course course) {
		mapper.update(course);
	}

	public Course csv(int id) {
		// TODO Auto-generated method stub
		return mapper.csv(id);
	}

	public Page<Course> paginationClick(String name, Date start_date, Date end_date, Pageable pageable) {
		System.out.println(name);
		List<Course> search_course = mapper.paginationClick(name, start_date, end_date,
				new RowBounds((int) pageable.getOffset(), pageable.getPageSize()));
		Long searchTotalClick = mapper.paginationClickAccount(name, start_date, end_date);
		return new PageImpl<>(search_course, pageable, searchTotalClick);

	}

	public Date convertStringToDate(String start_date) {
		Date startDate = null;
		try {
			if (start_date != null) {
				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
				startDate = sdFormat.parse(start_date);

			}
		} catch (ParseException e) {
			// e.printStackTrace();
		}
		return startDate;
	}

	public Page<Course> getListClick(String name, Date start_date, Date end_date, Pageable pageable) {
		if (name == null) {
			return getList(pageable);
		}
		if ("null".equals(name)) {
			return getList(pageable);
		} else {
			return paginationClick(name, start_date, end_date, pageable);
		}
	}

	public Course csvTest(int id) {

		return mapper.csvTest(id);
	}

	public Date formatStringToDate(String year, String month, String day, String hour, String minute) throws ParseException {
		String startDateStringFromat = year + "-" + month + "-" + day + " " + hour
				+ ":" + minute;
		SimpleDateFormat startDateOutput = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = startDateOutput.parse(startDateStringFromat);

		return date;
	}

	private final Path fileStorageLocation = Paths.get("src/main/resources/static/images/");

	public String storeFile(MultipartFile file) throws IOException {
		Path targetLocation = this.fileStorageLocation.resolve(file.getOriginalFilename());
		Files.copy(file.getInputStream(), targetLocation);
		
		return file.getOriginalFilename();
	}

	

}
