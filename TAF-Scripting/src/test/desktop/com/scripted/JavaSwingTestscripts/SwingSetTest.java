package com.scripted.JavaSwingTestscripts;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.scripted.Swingset3Objects.ButtonModel;
import com.scripted.Swingset3Objects.DialogModel;
import com.scripted.Swingset3Objects.FrameModel;
import com.scripted.Swingset3Objects.ListModel;
import com.scripted.Swingset3Objects.OptionPaneModel;
import com.scripted.Swingset3Objects.ScrollPaneModel;
import com.scripted.Swingset3Objects.TabbedPaneModel;
import com.scripted.Swingset3Objects.TableModel;
import com.scripted.Swingset3Objects.TextBoxModel;
import com.scripted.Swingset3Objects.ToggleButtonModel;
import com.scripted.Swingset3Objects.TreeModel;
import com.scripted.Swingset3Objects.WindowModel;
import com.scripted.javaswing.SwingJavaDriver;
import net.sourceforge.marathon.javadriver.JavaDriver;
import com.scripted.reporting.*;


@Listeners({ AllureListener.class })
public class SwingSetTest {
	private static final Logger LOGGER = Logger.getLogger(SwingSetAppTest.class);
	private static JavaDriver driver; 
	
	@Test
	private static void testSwingSetApp() throws Exception {
		driver = SwingJavaDriver.initJDriver("D:\\SwingSet3.bat");
		FrameModel frame = new FrameModel(driver);
		frame.testFrameElements();
		/*DialogModel dialogModel = new DialogModel(driver);
		dialogModel.testDialogElements();
		WindowModel windowModel = new WindowModel(driver);
		windowModel.testWindowElements();*/
		TabbedPaneModel tabModel = new TabbedPaneModel(driver);
		tabModel.test();
		ScrollPaneModel spModel = new ScrollPaneModel(driver);
		spModel.test();
		TableModel tblModel = new TableModel(driver);
		tblModel.test();
		TreeModel treeModel = new TreeModel(driver);
		treeModel.test();
		ListModel listModel = new ListModel(driver);
		listModel.test();
		ToggleButtonModel toggleModel = new ToggleButtonModel(driver);
		toggleModel.test();
		ButtonModel buttonModel = new ButtonModel(driver);
		buttonModel.test();
		TextBoxModel textModel = new TextBoxModel(driver);
		textModel.test();
		OptionPaneModel optionModel = new OptionPaneModel(driver);
		optionModel.test();
		/*ComboBoxModel cmbModel = new ComboBoxModel(driver);
		cmbModel.test();*/
		SwingJavaDriver.quitDriver();
	}

}
