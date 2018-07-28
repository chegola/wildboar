package com.wildboar.web.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.wildboar.domain.Student;
import com.wildboar.messaging.Greeting;
import com.wildboar.messaging.MessageProducer;
import com.wildboar.messaging.ProducerChannel;
import com.wildboar.repository.StudentRepository;

@RestController
@RequestMapping("/api")
public class ProducerResource {

	private MessageChannel channel;
	private MessageProducer messageProducer;
	private StudentRepository studentRepository;

	public ProducerResource(ProducerChannel channel, MessageProducer messageProducer, StudentRepository studentRepository) {
	    this.channel = channel.messageChannel();
		this.messageProducer = messageProducer;
		this.studentRepository = studentRepository;
	}

	@GetMapping("/greetings/{count}")
	@Timed
	public void produce(@PathVariable int count) {
		while (count > 0) {
			channel.send(MessageBuilder.withPayload(new Greeting().setMessage("Hello world!: " + count)).build());
			count--;
		}
	}

	@GetMapping("/notify/{studentId}")
	@Timed
	public void inform(@PathVariable String studentId) {
			Student student = studentRepository.findByStudentId(studentId);
			// messageProducer.send(studentId);			
			channel.send(MessageBuilder.withPayload(this.buildMessage(student)).build());
	}
	
	private Greeting buildMessage(Student student) {
		final Greeting greeting = new Greeting();
		
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		final LocalDateTime now = LocalDateTime.now();  
		final String message = "ID: " + student.getStudentId().toString() + " " + student.getName() + " " + student.getSurname() + " " + dtf.format(now); 
		greeting.setMessage(message); 
		return greeting;
	}
}
