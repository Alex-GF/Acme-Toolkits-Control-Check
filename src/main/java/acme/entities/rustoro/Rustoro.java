package acme.entities.rustoro;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.item.Item;
import acme.framework.datatypes.Money;
import acme.framework.entities.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Rustoro extends AbstractEntity{
	
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;
		
	// Attributes --------------------------------------------------------------
	
	
	@Pattern(regexp ="^[2-9]{1}[0-9]{1}[0-1]{1}[0-9]{1}[0-3]{1}[0-9]{1}:[A-Z,a-z,0-9,_]{6}$")
	@NotBlank
	protected String code;
	
	@NotBlank
	@Length(min = 1, max = 100)
	protected String name;
	
	@NotBlank
	@Length(min = 1, max = 255)
	protected String explanation;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date creationMoment;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date startDate;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date finishDate;
	
	@NotNull
	@Valid
	protected Money quota;
	
	@URL
	protected String moreInfo;
	
	// Relationships ----------------------------------------------------------
	
	@OneToOne(optional = true)
	@Valid
	protected Item item;
}
