package ui.auto.examples;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ui.auto.core.components.CheckBox;
import ui.auto.core.components.SelectComponent;
import ui.auto.core.components.WebComponent;
import ui.auto.core.data.generators.Data;
import ui.auto.core.data.generators.GeneratorType;
import ui.auto.core.pagecomponent.PageObject;

@XStreamAlias("form")
public class FormPageObject extends PageObject{
	@Data(type=GeneratorType.HUMAN_NAMES,value="<A>",alias="first")
	@FindBy(id="entry_311820602")
	WebComponent firstName;
	@Data(type=GeneratorType.HUMAN_NAMES,value="<S>",alias="last")
	@FindBy(id="entry_1396008427")
	WebComponent lastName;
	@Data(type=GeneratorType.ADDRESS,value="<#> <S>, <T> <K> <C> <O>")
	@FindBy(id="entry_1447188970")
	WebComponent address;
	@Data(value="${first}.${last}@gmail.com")
	@FindBy(id="entry_1453012221")
	WebComponent emailAddress;
	@Data(type=GeneratorType.CUSTOM_LIST, value="Amber,Black,Blue,Brown,Gray,Green,Hazel,Violet")
	@FindBy(id="entry_1264472189")
	SelectComponent eyeColor;
	@Data(type=GeneratorType.CUSTOM_LIST,value="false,true")
	@FindBy(id="group_1262363679_1")
	CheckBox facebook;
	@Data(type=GeneratorType.CUSTOM_LIST,value="false,true")
	@FindBy(id="group_1262363679_2")
	CheckBox twitter;
	@Data(type=GeneratorType.CUSTOM_LIST,value="false,true")
	@FindBy(id="group_1262363679_3")
	CheckBox linkedin;
	@Data(type=GeneratorType.CUSTOM_LIST,value="false,true")
	@FindBy(id="group_1262363679_4")
	CheckBox email;
	@FindBy(id="ss-submit")
	WebElement submitButton;
        
	public void fillForm(){
		autoFillPage();
		submitButton.click();
		waitForUrlToChange();
	}
	
	public static void main(String[] args) throws Exception{
		FormPageObject form=new FormPageObject();
		System.out.println(form.generateXML());
	}

}
