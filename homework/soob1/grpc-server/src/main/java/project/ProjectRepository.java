package project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ProjectRepository {

	private EntityManager em;

	public ProjectRepository() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("grpc-server");
		em = emf.createEntityManager();
	}

	public Project findById(Long id) {
		return em.find(Project.class, id);
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
			Project project = findById(id);
			em.remove(project);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
	}
}
