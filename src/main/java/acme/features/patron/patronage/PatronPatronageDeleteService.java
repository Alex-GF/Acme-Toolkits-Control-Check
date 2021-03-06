package acme.features.patron.patronage;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.patronage.Patronage;
import acme.entities.patronageReport.PatronageReport;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractDeleteService;
import acme.roles.Patron;
import acme.utils.AcceptedCurrencyLibrary;

@Service
public class PatronPatronageDeleteService implements AbstractDeleteService<Patron, Patronage>{

	@Autowired
	protected PatronPatronageRepository repository;
	
	@Override
	public boolean authorise(final Request<Patronage> request) {
		assert request != null;
		
		
		final boolean result;
		final int patronageId;
		final Patronage patronage;
		final Patron patron;
		
		patronageId = request.getModel().getInteger("id");
		patronage = this.repository.getPatronageById(patronageId);
		patron = patronage.getPatron();
		result = !patronage.isPublished() && request.isPrincipal(patron);

		return result;
	}

	@Override
	public void bind(final Request<Patronage> request, final Patronage entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity,errors,"status", "code", "legalStuff", "budget", "creationMoment", "startDate", "finishDate", "link","published");
		
	}

	@Override
	public void unbind(final Request<Patronage> request, final Patronage entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity,model,"status", "code", "legalStuff", "budget", "creationMoment", "startDate", "finishDate", "link","published");
		
	}

	@Override
	public Patronage findOne(final Request<Patronage> request) {
		assert request != null;
		
		Patronage result;
		int id;
		
		id = request.getModel().getInteger("id");
		result = this.repository.getPatronageById(id);
		
		return result;
	}

	@Override
	public void validate(final Request<Patronage> request, final Patronage entity, final Errors errors) {
		
		assert request != null;
		assert entity != null;
		assert errors != null;

		final List<String> acceptedCurrencies = AcceptedCurrencyLibrary.getAcceptedCurrencies(this.repository.findAcceptedCurrencies());

		
		if (errors.hasErrors("creationMoment") || errors.hasErrors("startDate")) {
			
			final Calendar calendar = Calendar.getInstance();
			Date minimumPeriod;
			
			calendar.setTime(entity.getCreationMoment());
			calendar.add(Calendar.MONTH, 1);
			minimumPeriod = calendar.getTime();
			
			errors.state(request, entity.getStartDate().after(minimumPeriod), "startDate", "patron.patronage.form.error.acceptedPeriodTime");
			
		}

		if(!errors.hasErrors("retailPrice")) {
			boolean acceptedCurrency;

			acceptedCurrency = acceptedCurrencies.contains(entity.getBudget().getCurrency());

			errors.state(request, acceptedCurrency, "budget", "patron.patronage.form.error.acceptedCurrency");

		}
		
		if (errors.hasErrors("code")) {
			Patronage existing;
			
			existing = this.repository.getPatronageByCode(entity.getCode());
			errors.state(request, existing == null || existing.getId() == entity.getId(), "code", "patron.patronage.form.error.duplicated");
		}
		
	}


	@Override
	public void delete(final Request<Patronage> request, final Patronage entity) {
		assert request != null;
		assert entity != null;
		
		Collection<PatronageReport> reports;

		reports = this.repository.findPatronageReportByPatronageId(entity.getId());
		for (final PatronageReport report : reports) {
			this.repository.delete(report);
		}
		
		this.repository.delete(entity);
		
	}
		
}
