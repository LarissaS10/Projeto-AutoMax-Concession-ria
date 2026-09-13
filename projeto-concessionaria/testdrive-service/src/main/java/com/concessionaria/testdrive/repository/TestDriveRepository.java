package com.concessionaria.testdrive.repository;

import com.concessionaria.testdrive.model.TestDrive;
import com.concessionaria.testdrive.model.TestDrive.StatusTestDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TestDriveRepository extends JpaRepository<TestDrive, Long> {

    List<TestDrive> findByStatus(StatusTestDrive status);

    List<TestDrive> findByCarroId(Long carroId);

    List<TestDrive> findByClienteNomeContainingIgnoreCase(String nome);

    List<TestDrive> findByDataAgendadaBetween(LocalDateTime inicio, LocalDateTime fim);

    long countByStatus(StatusTestDrive status);
}