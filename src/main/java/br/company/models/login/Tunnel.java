package br.company.models.login;

import javax.persistence.*;

@Entity
@Table(name = "tunnel")
public class Tunnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String db_schema;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDb_schema() {
        return db_schema;
    }

    public void setDb_schema(String db_schema) {
        this.db_schema = db_schema;
    }
}