package acme.entities.announcement;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.entities.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Announcement extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				creationMoment;

	@NotBlank
	@Length(min = 1, max = 100)
	protected String			title;

	@NotBlank
	@Length(min = 1, max = 255)
	protected String			body;
	
	protected boolean criticalFlag;
	
	@URL
	protected String link;

	

}
