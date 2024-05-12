package com.mybatiscrud.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mybatiscrud.model.Course;
import com.mybatiscrud.model.Module;
import com.mybatiscrud.model.Teacher;
import com.mybatiscrud.service.CourseService;
import com.mybatiscrud.service.FolderRefreshService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CourseController {

	@Autowired
	CourseService service;

	@Autowired
	private FolderRefreshService folderRefreshService;

	@GetMapping("/course/create")
	public String create(Model model, @ModelAttribute Course course) {
		model.addAttribute("course", course);
		return "/courses/insert";
	}

	@PostMapping("/course/insert")
	public <ColorForm> String insert(Model model, @Validated @ModelAttribute Course course, BindingResult result,
			@RequestParam(name = "course_img", required = false) MultipartFile file,
			@RequestParam("startYear") String startYear, @RequestParam("startMonth") String startMonth,
			@RequestParam("startDay") String startDay, @RequestParam("startHour") String startHour,
			@RequestParam("startMinute") String startMinute, @RequestParam("endYear") String endYear,
			@RequestParam("endMonth") String endMonth, @RequestParam("endDay") String endDay,
			@RequestParam("endHour") String endHour, @RequestParam("endMinute") String endMinute,
			@RequestParam("color") String color) throws IOException, ParseException {

		Date start_date = service.formatStringToDate(startYear, startMonth, startDay, startHour, startMinute);
		Date end_date = service.formatStringToDate(endYear, endMonth, endDay, endHour, endMinute);
		// String filename = file.getOriginalFilename();
		String fileName = service.storeFile(file);
		course.setCourse_img(fileName);
		course.setStart_date(start_date);
		course.setEnd_date(end_date);
		course.setColor(color);
		folderRefreshService.refreshFolder();
		service.insert(course);
		return "/courses/insert";

		// String UPLOADED_FOLDER = "src/main/resources/static/images/";
		// byte[] bytes = file.getBytes();
		// Path path = Paths.get(UPLOADED_FOLDER, file.getOriginalFilename());
		// Files.write(path, bytes);

	}

	@GetMapping("/")
	public String index(Model model, @ModelAttribute Course course, @PageableDefault(size = 5) Pageable pageable,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "start", required = false) String start,
			@RequestParam(name = "end", required = false) String end, HttpServletResponse response) {
		Date start_date = service.convertStringToDate(start);
		Date end_date = service.convertStringToDate(end);
		int totalDisplayedPages = 1; // Total number of links to display
		int boundaryPages = 1; // Number of pages to display at the start and end
		int surroundingPages = 1; // Number of pages surrounding the current page
		Page<Course> click_search = service.getListClick(name, start_date, end_date, pageable);
		Course c = new Course();
		c.setName(name);
		// c.setStart_date(start_date);
		c.setEnd_date(end_date);

		if ("null".equals(name)) {
			model.addAttribute("name", "");
		} else {
			model.addAttribute("name", c.getName());
		}
		model.addAttribute("start_date", start);
		model.addAttribute("end_date", end);
		model.addAttribute("totalDisplayedPages", totalDisplayedPages);
		model.addAttribute("boundaryPages", boundaryPages);
		model.addAttribute("surroundingPages", surroundingPages);
		model.addAttribute("courses", click_search);
		// return "/courses/index";
		return "/courses/index";

	}

	@PostMapping("/course/search")
	public String serach(Model model, @ModelAttribute Course course, @PageableDefault(size = 5) Pageable pageable,
			BindingResult bindingResult) {
		Page<Course> search = service.search(course, pageable);
		String startDate = null;
		String endDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (course.getStart_date() != null) {
			startDate = formatter.format(course.getStart_date());
			model.addAttribute("start_date", startDate);
			model.addAttribute("name", course.getName());
		}
		if (course.getEnd_date() != null) {
			endDate = formatter.format(course.getEnd_date());
			model.addAttribute("end_date", endDate);

		} else {
			model.addAttribute("name", course.getName());

		}
		model.addAttribute("courses", search);

		return "/courses/index";

	}

	@GetMapping("/course/edit/{id}")
	public String edit(Model model, @ModelAttribute Course course, @PathVariable(value = "id") int id) {

		Course course_edit = service.edit(id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH mm");
		String start_date = sdf.format(course_edit.getStart_date());
		String end_date = sdf.format(course_edit.getEnd_date());
		String[] split_start_date = start_date.split(" ");
		String start_year = split_start_date[0];
		String start_month = split_start_date[1];
		String start_day = split_start_date[2];
		String start_hour = split_start_date[3];
		String start_minute = split_start_date[4];
		
		String[] split_end_date = end_date.split(" ");
		String end_year = split_end_date[0];
		String end_month = split_end_date[1];
		String end_day = split_end_date[2];
		String end_hour = split_end_date[3];
		String end_minute = split_end_date[4];
		
		model.addAttribute("startYear", start_year);
		model.addAttribute("startMonth", start_month);
		model.addAttribute("startDay", start_day);
		model.addAttribute("startHour", start_hour);
		model.addAttribute("startMinute", start_minute);
		model.addAttribute("endYear", end_year);
		model.addAttribute("endMonth", end_month);
		model.addAttribute("endDay", end_day);
		model.addAttribute("endHour", end_hour);
		model.addAttribute("endMinute", end_minute);
		model.addAttribute("course", course_edit);

		return "/courses/edit";
	}

	@PostMapping("/course/{id}/update")
	public String update(Model model, @ModelAttribute Course course, @PathVariable(value = "id") int id,
			@RequestParam(name = "course_img", required = false) MultipartFile file,
			@RequestParam("startYear") String startYear, @RequestParam("startMonth") String startMonth,
			@RequestParam("startDay") String startDay, @RequestParam("startHour") String startHour,
			@RequestParam("startMinute") String startMinute, @RequestParam("endYear") String endYear,
			@RequestParam("endMonth") String endMonth, @RequestParam("endDay") String endDay,
			@RequestParam("endHour") String endHour, @RequestParam("endMinute") String endMinute) throws ParseException {
		Date start_date = service.formatStringToDate(startYear, startMonth, startDay, startHour, startMinute);
	    Date end_date = service.formatStringToDate(endYear, endMonth, endDay, endHour, endMinute);
		course.setStart_date(start_date);
		course.setEnd_date(end_date);
		model.addAttribute("startYear", startYear);
		model.addAttribute("startMonth", startMonth);
		model.addAttribute("startDay", startDay);
		model.addAttribute("startHour", startHour);
		model.addAttribute("startMinute", startMinute);
		model.addAttribute("endYear", endYear);
		model.addAttribute("endMonth", endMonth);
		model.addAttribute("endDay", endDay);
		model.addAttribute("endHour", endHour);
		model.addAttribute("endMinute", endMinute);
		service.update(course);
		
		return "/courses/edit";
	}

	@GetMapping("/course/csv/{id}")
	public ResponseEntity<byte[]> csv(@PathVariable(value = "id") int id) {
		Course course = service.csvTest(id);
		// Generate CSV content
		byte[] csvContent = generateCsvContent(course);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("text/csv"));
		headers.setContentDispositionFormData("attachment", "courses.csv");
		return ResponseEntity.ok().headers(headers).body(csvContent);

	}

	private byte[] generateCsvContent(Course course) {
		StringBuilder csvBuilder = new StringBuilder();
		csvBuilder.append("Course Id,Course Name,Module Id,Module Name, Teacher ID, Teacher Name ID\n");
		csvBuilder.append(course.getId()).append(",");
		csvBuilder.append(course.getName()).append("\n");
		for (Module module : course.getModule()) {
			List<Integer> teacherId = new ArrayList<>();
			List<String> teacherNames = new ArrayList<>();
			for (Teacher teacher : module.getTeacher()) {
				teacherId.add(teacher.getId());
				teacherNames.add(teacher.getName());
			}
			csvBuilder.append(module.getId()).append(",");
			csvBuilder.append(module.getName()).append("\n");
			String formattedData = IntStream.range(0, teacherId.size())
					.mapToObj(i -> teacherId.get(i) + "," + teacherNames.get(i)).collect(Collectors.joining("\n"));
			csvBuilder.append(formattedData);
			csvBuilder.append("\n");
		}
		return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
	}

	@GetMapping("/course/clear")
	public String clear() {
		return "redirect:/";
	}

	@GetMapping("/course/back")
	public String back() {
		return "redirect:/";
	}

}
