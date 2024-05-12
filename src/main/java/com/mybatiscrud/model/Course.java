package com.mybatiscrud.model;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class Course {

	private int id;
	private String name;
	private String group_name;

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	@DateTimeFormat(pattern = "yyyy-MM-ddHH:mm")
	public Date start_date;

	@DateTimeFormat(pattern = "yyyy-MM-ddHH:mm")
	private Date end_date;
	private int duration;
	private int fees;
	private String color;
	private String available;
	private String course_img;

	public String getCourse_img() {
		return course_img;
	}

	public void setCourse_img(String course_img) {
		this.course_img = course_img;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	private List<Module> module;

	public List<Module> getModule() {
		return module;
	}

	public void setModule(List<Module> module) {
		this.module = module;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getFees() {
		return fees;
	}

	public void setFees(int fees) {
		this.fees = fees;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", start_date=" + start_date + ", end_date=" + end_date
				+ ", duration=" + duration + ", fees=" + fees + ", available=" + available + ", course_img="
				+ course_img + ", module=" + module + "]";
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
