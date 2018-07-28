package com.wildboar.messaging;

import org.springframework.stereotype.Service;

import com.wildboar.repository.StudentRepository;

@Service
public class MessageProducerImpl implements MessageProducer {

	
	private StudentRepository studentRepository; 
		
	public MessageProducerImpl(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}


	@Override
	public void send(Long studentId) {
		
	}
	
}
