package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository ------------------------------
	@Autowired
	private SocialProfileRepository socialProfileRepository;

	// Supporting services -----------------------
	@Autowired
	private ActorService actorService;

	// /CREATE
	public SocialProfile create() {
		SocialProfile result;

		result = new SocialProfile();

		return result;
	}

	// /FINDONE
	public SocialProfile findOne(final int socialProfileId) {
		SocialProfile result;

		result = this.socialProfileRepository.findOne(socialProfileId);
		Assert.notNull(result);

		return result;
	}

	// //SAVE

	public SocialProfile save(final SocialProfile socialProfile) {
		SocialProfile result;
		Actor principal;

		Assert.notNull(socialProfile);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		if (socialProfile.getId() != 0) {
			Assert.isTrue(principal.getSocialProfile().contains(socialProfile));
		}

		result = this.socialProfileRepository.save(socialProfile);
		Assert.notNull(result);

		if (socialProfile.getId() == 0) {
			principal.getSocialProfile().add(result);
		}

		return result;
	}

	// DELETE

	public void delete(final SocialProfile socialProfile) {
		Actor principal;

		Assert.notNull(socialProfile);
		Assert.isTrue(socialProfile.getId() != 0);
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.getSocialProfile().contains(socialProfile));
		principal.getSocialProfile().remove(socialProfile);

		this.socialProfileRepository.delete(socialProfile);

	}

	public Integer socialProfilesInSystem(){
		return this.socialProfileRepository.socialProfilesInSystem();
	}
}
