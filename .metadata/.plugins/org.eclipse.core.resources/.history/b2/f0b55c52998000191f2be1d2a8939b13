package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.FinderService;

@Controller
@RequestMapping(value = "statistics/administrator")
public class DashboardController extends AbstractController{
	
	
	@Autowired
	private FinderService finderService;
	
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
	
	
		result = new ModelAndView("administrator/statistics");
		

		result.addObject("requestURI", "statistics/administrator/display.do");
		return result;
	}
	

}
