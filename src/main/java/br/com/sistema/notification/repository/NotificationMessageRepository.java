package br.com.sistema.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sistema.notification.entity.NotificationMessage;

@Repository
public interface NotificationMessageRepository extends JpaRepository<NotificationMessage, Long> {

    // ====================================================
    // Retorna todas as notificações paginadas, ordenadas pela mais recente
    // ====================================================
    Page<NotificationMessage> findAllByOrderByPublishedAtDesc(Pageable pageable);
}
