package {{package}};

import org.robovm.cocoatouch.foundation.NSAutoreleasePool;
import org.robovm.cocoatouch.foundation.NSDictionary;
import org.robovm.cocoatouch.uikit.UIApplication;
import org.robovm.cocoatouch.uikit.UIApplicationDelegate;

public class {{class-name}} extends UIApplicationDelegate.Adapter {
	public boolean didFinishLaunching(UIApplication app, NSDictionary opts) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(args, null, {{class-name}}.class);
		pool.drain();
	}
}
