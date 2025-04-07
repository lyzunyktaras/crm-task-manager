package com.sample.crm;

import com.sample.crm.dto.ContactDTO;
import com.sample.crm.dto.TaskDTO;
import com.sample.crm.entity.Contact;
import com.sample.crm.entity.Task;
import com.sample.crm.repository.ContactRepository;
import com.sample.crm.repository.TaskRepository;
import com.sample.crm.service.ContactService;
import com.sample.crm.service.TaskService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisIntegrationTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    @Order(1)
    void shouldCacheFindAllTasksAndContacts() {
        Task task = new Task();
        task.setDescription("Test Task");
        taskRepository.save(task);

        List<TaskDTO> firstTaskCall = taskService.findAll();
        List<TaskDTO> secondTaskCall = taskService.findAll();

        assertThat(firstTaskCall).isEqualTo(secondTaskCall);
        assertThat(cacheManager.getCache("tasks").get(SimpleKey.EMPTY)).isNotNull();

        Contact contact = new Contact();
        contact.setFirstName("First");
        contact.setLastName("Last");
        contactRepository.save(contact);

        List<ContactDTO> firstContactCall = contactService.findAll();
        List<ContactDTO> secondContactCall = contactService.findAll();

        assertThat(firstContactCall).isEqualTo(secondContactCall);
        assertThat(cacheManager.getCache("contacts").get(SimpleKey.EMPTY)).isNotNull();
    }

    @Test
    @Order(2)
    void shouldEvictCacheOnTaskAndContactUpdate() {
        Task task = new Task();
        task.setDescription("Task to Update");
        taskRepository.save(task);

        taskService.findAll();

        taskService.delete(task.getId());

        assertThat(cacheManager.getCache("tasks").get(SimpleKey.EMPTY)).isNull();

        Contact contact = new Contact();
        contact.setFirstName("Jane");
        contact.setLastName("Doe");
        contactRepository.save(contact);

        contactService.findAll();

        contactService.delete(contact.getId());

        assertThat(cacheManager.getCache("contacts").get(SimpleKey.EMPTY)).isNull();
    }
}
