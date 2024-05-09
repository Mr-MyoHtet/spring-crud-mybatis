package com.mybatiscrud.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
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

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CourseController {

	@Autowired
	CourseService service;

	@GetMapping("/course/create")
	public String create(Model model, @ModelAttribute Course course) {
		model.addAttribute("course", course);
		return "/courses/insert";
	}

	@PostMapping("/course/insert")
	public String insert(Model model, @Validated @ModelAttribute Course course, BindingResult result,
			@RequestParam(name = "course_img", required = false) MultipartFile file, @RequestParam("year") String year,
			@RequestParam("month") int month, @RequestParam("day") int day, @RequestParam("hour") int hour,
			@RequestParam("minute") int minute) throws IOException, ParseException {

		// String UPLOADED_FOLDER = "src/main/resources/static/images/";
		// byte[] bytes = file.getBytes();
		// Path path = Paths.get(UPLOADED_FOLDER, file.getOriginalFilename());
		// Files.write(path, bytes);
		Date startDate = service.formatStringToDate(year, month, day, hour, minute);
		String filename = file.getOriginalFilename();
		course.setCourse_img(filename);
		course.setStart_date(startDate);
		service.insert(course);

		return "/courses/insert";
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
