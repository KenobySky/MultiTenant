package br.company.repository.login;

import br.company.models.login.Tunnel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TunnelRepository extends JpaRepository<Tunnel, Long> {
}