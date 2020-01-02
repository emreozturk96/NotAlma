package com.emre.controller;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.emre.entity.Note;
import com.emre.security.LoginFilter;
import com.emre.service.MailService;
import com.emre.service.NoteService;

@Controller
public class HomeController {
	
	public static String url="http://localhost:8080/notalma";
	
	@Autowired
	NoteService noteService;	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(HttpServletRequest req, Model model) {	
		return "redirect:/index";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index2(HttpServletRequest req, Model model) {	
		return "redirect:/index";
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest req, Model model) {	
		
		model.addAttribute("user",req.getSession().getAttribute("user"));
		
		System.out.println(req.getRemoteAddr());
		model.addAttribute("baslik","Not Alma");	
		return "index";
	}
	
	@RequestMapping(value = "/detay/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Long id,HttpServletRequest req, Model model) {	
		
		model.addAttribute("user",req.getSession().getAttribute("user"));
		
		model.addAttribute("id",id);
		return "detail";
	}
	
	@RequestMapping(value = "/error_404", method = RequestMethod.GET)
	public String error404(Model model) {	
		return "error_404";
	}
	
	@RequestMapping(value = "/ekle", method = RequestMethod.GET)
	public String ekle(Model model,HttpServletRequest req) {	
		model.addAttribute("user",req.getSession().getAttribute("user"));
		return "addNote";
	}
	
	@RequestMapping(value="/addNote", method=RequestMethod.POST)
	public ResponseEntity<String> addNote(@RequestBody Note note, HttpServletRequest request){
		System.out.println(note.toString());
		noteService.createNote(note,request);
		return new ResponseEntity<>("OK",HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/getNotes", method=RequestMethod.POST)
	public ResponseEntity<ArrayList<Note>> getNotes(HttpServletRequest request){
		return new ResponseEntity<>(noteService.getAll(LoginFilter.user.getId()),HttpStatus.OK);
	}
	
	@RequestMapping(value="/getNote", method=RequestMethod.POST)
	public ResponseEntity<Note> getNote (@RequestBody String id,HttpServletRequest request){
		
		Note note = noteService.getNoteFindById(Long.parseLong(id));
		if(note.getUser_id() == LoginFilter.user.getId()) {
			return new ResponseEntity<>(noteService.getNoteFindById(Long.parseLong(id)),HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null,HttpStatus.OK);
	}
	
	@RequestMapping(value="/updateNote", method=RequestMethod.POST)
	public ResponseEntity<String> updateNote(@RequestBody Note note, HttpServletRequest request){
		Note oldNote = noteService.getNoteFindById(note.getId());
		oldNote.setTitle(note.getTitle());
		oldNote.setContent(note.getContent());
		noteService.updateNote(oldNote, request);
		return new ResponseEntity<>("OK",HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/deleteNote", method=RequestMethod.POST)
	public ResponseEntity<String> deleteNote(@RequestBody Note note, HttpServletRequest request){
		Note oldNote = noteService.getNoteFindById(note.getId());
	
		noteService.deleteNote(oldNote, request);
		return new ResponseEntity<>("OK",HttpStatus.OK);
	}
	

}
