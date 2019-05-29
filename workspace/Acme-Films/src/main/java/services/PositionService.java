package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionRepository;
import domain.Actor;
import domain.Position;
import domain.SystemConfiguration;

@Service
@Transactional
public class PositionService {

	// Managed repository ------------------------------
	@Autowired
	private PositionRepository positionRepository;

	// Supporting services -----------------------

	@Autowired
	private ActorService	actorService;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private Validator validator;

	// CRUD methods -----------------------------------

	public Position create() {
		Position result;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");

		result = new Position();
		
		result.setChildPositions(new ArrayList<Position>());

		return result;
	}
	
	public Position findOne(final int positionId) {
		Position result;

		result = this.positionRepository.findOne(positionId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Position> findAll() {
		Collection<Position> result;

		result = this.positionRepository.findAll();
		Assert.notNull(result);

		return result;
	}
	
	public Position save(final Position position) {
		Position result;
		final Position parent, root;
		SystemConfiguration systemConf;
		Set<String> idiomasPosition;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");
		
		root = this.findRoot();
		Assert.isTrue(position.getId() != root.getId());
		Assert.notNull(position.getParentPosition());
		Assert.notNull(position.getName());
		
		systemConf = systemConfigurationService.findMySystemConfiguration();
		Set<String> idiomasSystemConf = new HashSet<String>(systemConf
				.getWelcomeMessage().keySet());
		idiomasPosition = position.getName().keySet();
		Assert.isTrue(idiomasSystemConf.equals(idiomasPosition));

		result = this.positionRepository.save(position);
		Assert.notNull(result);
		
		parent = result.getParentPosition();

		// Si a�n no est� guardado en la bbdd, actualizamos las categor�as hija de su padre
		if (position.getId() == 0)
			this.newChild(parent, result);
		else if (!position.getParentPosition().equals(parent)) {
			this.deleteChild(position.getParentPosition(), position);
			this.newChild(parent, position);
		}
		return result;
	}

	public void delete(final Position position) {
		Position parent, root, aux;
		Collection<Position> childPositions;
		Actor principal;
		boolean canBeDeleted = true;
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "MODERATOR"),
				"not.allowed");
		
		Assert.notNull(position);
		Assert.isTrue(position.getId() != 0);

		// Comprobamos que no vamos a borrar la categor�a ROOT
		root = this.findRoot();
		Assert.isTrue(position.getId() != root.getId(), "root.position"); 
		
		Assert.isTrue(canBeDeleted, "position.used");

		childPositions = position.getChildPositions();
		parent = position.getParentPosition();

		if (!childPositions.isEmpty())
			for (final Position pos : childPositions)
				pos.setParentPosition(parent);

		this.deleteChild(parent, position);
		
		this.positionRepository.delete(position);
	}

	// Other business methods
	
	public Position reconstruct(Position position, String nameES,
			String nameEN, BindingResult binding) {
		Position res;

		if (position.getId() == 0) {
			position.setName(new HashMap<String, String>());

			position.getName().put("Espa�ol", nameES);
			position.getName().put("English", nameEN);
			
			res = position;
		} else {
			res = this.positionRepository.findOne(position.getId());

			position.setName(new HashMap<String, String>());

			position.getName().put("Espa�ol", nameES);
			position.getName().put("English", nameEN);

			res.setName(position.getName());
			
		}
		this.validator.validate(res, binding);

		return res;
	}

	public Collection<Position> findChildPositions(final int positionId) {
		Collection<Position> result;

		result = this.positionRepository.findChildPositions(positionId);

		return result;
	}

	public Position findRoot() {
		Position result;

		result = this.positionRepository.findRoot();

		return result;
	}

	private void newChild(final Position position, final Position child) {
		Collection<Position> childPositions;

		childPositions = position.getChildPositions();
		childPositions.add(child);
		position.setChildPositions(childPositions);
	}

	private void deleteChild(final Position position, final Position child) {
		Collection<Position> childPositions;

		childPositions = position.getChildPositions();
		childPositions.remove(child);
		position.setChildPositions(childPositions);
	}
}

