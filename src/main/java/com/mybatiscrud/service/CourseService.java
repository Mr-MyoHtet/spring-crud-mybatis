package com.mybatiscrud.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.mybatiscrud.mapper.CourseMapper;
import com.mybatiscrud.model.Course;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;

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

	public List<Course> edit(int id) {
		return mapper.edit(id);
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

	public Date formatStringToDate(String year, int month, int day, int hour, int minute) throws ParseException {
		String monthStringFormat = null;
		String dayStringFormat = null;
		String hourStringFormat = null;
		String minuteStringFormat = null;
		if (month < 10) {
			monthStringFormat = "0" + month;
		} else {
			monthStringFormat =String.valueOf(month);
		}
		if (day < 10) {
			dayStringFormat = "0" + day;
		} else {
			dayStringFormat =  String.valueOf(day);
		}
		if (hour < 10) {
			hourStringFormat = "0" + hour;
		}
		else {
			hourStringFormat = String.valueOf(hour);
		}
		if (minute < 10) {
			minuteStringFormat = "0" + minute;
		}
		else {
			minuteStringFormat = String.valueOf(minute);
		}

		String dateString = year + "-" + monthStringFormat + "-" + dayStringFormat + " " + hourStringFormat + ":"
				+ minuteStringFormat;

		Date formatStartDate = null;
		// dataStirng input to Date Format
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		formatStartDate = sdFormat.parse(dateString);

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String formattedDate = inputFormat.format(formatStartDate);
		Date startDate = inputFormat.parse(formattedDate);

		return startDate;
	}

}
