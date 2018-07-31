package com.wildboar.messaging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.wildboar.domain.ParentStudent;
import com.wildboar.domain.SocialUserConnection;
import com.wildboar.domain.Student;
import com.wildboar.repository.ParentStudentRepository;
import com.wildboar.repository.SocialUserConnectionRepository;
import com.wildboar.repository.StudentRepository;
import com.wildboar.social.linecorp.controller.LineWebhook;

@Service
public class ConsumerService {
	private final Logger log = LoggerFactory.getLogger(ConsumerService.class);

	private ParentStudentRepository parentStudentRepository;	
	private StudentRepository studentRepository;
	private LineWebhook lineWebhook;
	private SocialUserConnectionRepository socialUserConnectionRepository;
	
	
	public ConsumerService(ParentStudentRepository parentStudentRepository, StudentRepository studentRepository, 
			LineWebhook lineWebhook, SocialUserConnectionRepository socialUserConnectionRepository) {
		this.parentStudentRepository = parentStudentRepository;
		this.studentRepository = studentRepository;
		this.lineWebhook = lineWebhook;
		this.socialUserConnectionRepository = socialUserConnectionRepository;
	}


	@StreamListener(ConsumerChannel.CHANNEL)
	public void consume(StudentCheckIn studentCheckIn) {
		// log.info("Received message: {}.", greeting.getMessage());
		log.info("Received message: {}.", studentCheckIn.getStudentId() + "-" + studentCheckIn.getCheckedIn());
		
		final Student student = studentRepository.findByStudentId(studentCheckIn.getStudentId());
		
		final String text =  student.getName() + " " + student.getSurname() + " ID:" + studentCheckIn.getStudentId() + " has been checked-in at " + studentCheckIn.getCheckedIn();
		final List<ParentStudent> parentStudents = getParents(student);
		for (ParentStudent parentStudent : parentStudents) {
			final SocialUserConnection socialUserConnection = socialUserConnectionRepository.findOneByUserIdAndProviderId(parentStudent.getUser().getLogin(), "LINE");
			lineWebhook.sendMessage(socialUserConnection.getProviderUserId(), text);
		}
	} 
	
	private List<ParentStudent> getParents(Student student) {
		
		final List<ParentStudent> parentStudents = parentStudentRepository.findByStudent(student);
		return parentStudents;
	}
}
