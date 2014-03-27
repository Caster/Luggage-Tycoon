package accg;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import accg.io.PackageAccgIoSuite;
import accg.utils.PackageAccgUtilsSuite;

@RunWith(Suite.class)
@SuiteClasses({
	PackageAccgSuite.class,
	PackageAccgIoSuite.class,
	PackageAccgUtilsSuite.class
})
public final class AllTestsSuite {}
