package ui.auto.core.pagecomponent;

import org.openqa.selenium.WebElement;

public abstract class PageComponentNoDefaultAction extends PageComponent {

	public PageComponentNoDefaultAction(String data, String initialData) {
		super(data, initialData);
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
	public void validateInitialData() {
	}

	@Override
	public void validateData() {
	}


}
