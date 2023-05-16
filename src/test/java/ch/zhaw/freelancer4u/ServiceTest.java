package ch.zhaw.freelancer4u;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobCreateDTO;
import ch.zhaw.freelancer4u.model.JobState;
import ch.zhaw.freelancer4u.model.JobType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private HttpHeaders headers;

  @BeforeEach
  public void setUp() {
    headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
  }

  @Test
  @DisplayName("Create job test")
  public void createJobTest() {
    JobCreateDTO dto = new JobCreateDTO("Test job", 1000.0, JobType.TEST);
    HttpEntity<JobCreateDTO> request = new HttpEntity<>(dto, headers);
    ResponseEntity<Job> response = restTemplate.exchange(
      "http://localhost:" + port + "/api/job",
      HttpMethod.POST,
      request,
      Job.class
    );
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Job job = response.getBody();
    assertNotNull(job.getId());
    assertEquals(dto.getDescription(), job.getDescription());
    assertEquals(dto.getEarnings(), job.getEarnings());
    assertEquals(dto.getJobType(), job.getJobType());
    assertEquals(JobState.NEW, job.getJobState());
  }

  @Test
  @DisplayName("Create a new job and verify it is returned in the job list")
  public void createJobAndVerifyInList() {
    String token = UUID.randomUUID().toString();
   
    JobCreateDTO jobCreateDTO = new JobCreateDTO(token, 1000.0, JobType.TEST);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    ResponseEntity<Job> createJobResponse = restTemplate.exchange(
      "/api/job",
      HttpMethod.POST,
      new HttpEntity<>(jobCreateDTO, headers),
      Job.class
    );
    assertEquals(HttpStatus.CREATED, createJobResponse.getStatusCode());
    assertNotNull(createJobResponse.getBody());

    ResponseEntity<List<Job>> jobListResponse = restTemplate.exchange(
      "/api/job",
      HttpMethod.GET,
      null,
      new ParameterizedTypeReference<List<Job>>() {}
    );
    assertEquals(HttpStatus.OK, jobListResponse.getStatusCode());
    assertNotNull(jobListResponse.getBody());

    boolean foundJob = false;
    for (Job job : jobListResponse.getBody()) {
      if (job.getDescription().equals(token)) {
        foundJob = true;
        break;
      }
    }
    assertTrue(foundJob, "Created job not found in the job list");
  }
 // delete is not working
  
}
