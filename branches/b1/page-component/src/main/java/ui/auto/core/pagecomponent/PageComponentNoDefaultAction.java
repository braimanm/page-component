package ui.auto.core.pagecomponent;

import org.openqa.selenium.WebElement;

import ui.auto.core.data.DataTypes;

public abstract class PageComponentNoDefaultAction extends PageComponent {

	public PageComponentNoDefaultAction() {
	}
	
	public PageComponentNoDefaultAction(WebElement coreElement) {
		super(coreElement);
	}

	@Override
	public void setValue() {
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public void validateData(DataTypes validationMethod) {
	}

}
