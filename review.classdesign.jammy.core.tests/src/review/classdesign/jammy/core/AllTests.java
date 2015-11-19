package review.classdesign.jammy.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import review.classdesign.jammy.core.common.NamedObjectTest;
import review.classdesign.jammy.core.common.RequestUtilsTest;
import review.classdesign.jammy.core.model.ContestTest;
import review.classdesign.jammy.core.model.RoundTest;

@RunWith(Suite.class)
@SuiteClasses({
	// review.classdesign.jammy.core.common test cases.
	NamedObjectTest.class,
	RequestUtilsTest.class,
	// review.classdesign.jammy.core.model test cases.
	ContestTest.class,
	RoundTest.class
})
public final class AllTests {

}
