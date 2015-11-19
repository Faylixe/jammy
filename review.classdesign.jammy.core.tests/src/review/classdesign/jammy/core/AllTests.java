package review.classdesign.jammy.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import review.classdesign.jammy.core.common.NamedObjectTest;
import review.classdesign.jammy.core.common.RequestUtilsTest;
import review.classdesign.jammy.core.common.SerializationUtilsTest;
import review.classdesign.jammy.core.common.TemplateTest;

@RunWith(Suite.class)
@SuiteClasses({
	// review.classdesign.jammy.core.common test cases.
	NamedObjectTest.class,
	RequestUtilsTest.class,
	SerializationUtilsTest.class,
	TemplateTest.class
})
public final class AllTests {

}
