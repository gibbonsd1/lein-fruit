package {{package}};

import clojure.lang.RT;
import clojure.lang.Symbol;

import org.robovm.cocoatouch.foundation.NSAutoreleasePool;
import org.robovm.cocoatouch.foundation.NSDictionary;
import org.robovm.cocoatouch.uikit.UIApplication;
import org.robovm.cocoatouch.uikit.UIApplicationDelegate;

public class {{class-name}} extends UIApplicationDelegate.Adapter {
	public boolean didFinishLaunching(UIApplication app, NSDictionary opts) {
		RT.var("clojure.core", "require").invoke(Symbol.intern("{{namespace}}"));
		RT.var("{{namespace}}", "init").invoke();
		return true;
	}

	public static void main(String[] args) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(args, null, {{class-name}}.class);
		pool.drain();
	}
}
