package project;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

public class ProjectRepository {

	private final EntityManager em;

	public ProjectRepository() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("grpc-server");
		em = emf.createEntityManager();
	}

	public Optional<Project> findById(Long id) {
		return Optional.ofNullable(em.find(Project.class, id));
	}

	public List<Project> findByMemberId(Long memberId) {
		return em.createQuery("select p from Project p where p.memberId = " + memberId, Project.class)
				.getResultList();
	}

	public Project save(Project project) {
		EntityTransaction transaction = em.getTransaction();

		try {
			transaction.begin();
			em.persist(project);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
		return project;
	}

	public void deleteById(Long id) {
		EntityTransaction transaction = em.getTransaction();

		try {
			transaction.begin();
			findById(id).ifPresent(em::remove);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
	}
}
