package ua.illia.estore.model.product;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_pk_sequence_generator")
    @SequenceGenerator(name = "images_pk_sequence_generator", sequenceName = "images_pk_sequence", allocationSize = 1)
    private long id;

    private String path;

    private String externalId;
}
